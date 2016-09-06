dojo.provide("vf.broker.task.action.Save");

/**
 * Post data to the server.
 * 
 */
dojo.declare("vf.broker.task.action.Save", null, {

	/**
	 * The vf.broker.task.View object.
	 */
	_view: null,
	
	constructor: function(view){
		this._view = view;
	},
	
	/*
	 * Post data to be saved to the server.
	 * @param {Object} the data that will be serialized to JSON and posted to the server.
	 */
	_executeXhrRequest: function(data){
		var scope = this;
		
		var requestArgs = {
			failOk: true, // avoids console log of unreadable Error object
			url: this._view.getApplicationPath(window.location) + "/home/save.htm",
			handleAs: "json",
			headers:{"Content-Encoding":"UTF-8","Content-Type":"text/plain;charset=UTF-8"}
		};
		
		requestArgs.postData = dojo.toJson(data);
		
		if(requestArgs.form || requestArgs.content){
			requestArgs.headers = {"Content-Encoding":"UTF-8","Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"};
		}
		
		//can be extended later on to support headers and etags as needed
		var promise = dojo["xhrPost"](requestArgs);
		
		promise.then( function(responseVO, ioArgs){
				scope.handleRequestCallback(responseVO);
			}, 
			function(ex, ioArgs){
				alert("Save Error");
			});
	},
	
	/**
	 * Handles the response from the server.
	 * @param {response} Object returned by server.
	 */
	handleRequestCallback: function(response){
		try{
			this._view.setGridStore();
		}
		catch(ex){
			alert("Save error.")
		}
		
	},
	
	dump: function(item, store){
		return this._view.dump(item, store);
	},
	
	/**
	 * Gather data to be sent to the server and post any changes.
	 */
	execute: function(){
		var g = this._view.getGrid();
		var s = g.get("store");
		var data = [];
		
		var scope = this;
		
		//Get any new or updated items.
		var onComplete = function(items, request){
			dojo.forEach(items, function(item){
				if(s.isDirty(item)){
					data.push(scope.dump(item, s));
				};
			});
		}
		s.fetch({onComplete: onComplete});

		//Get items that have been deleted.
		var deletedItems = this._view._deletedItems;
		if(deletedItems){
			dojo.forEach(deletedItems, function(item){data.push(item)});
		}
		
		//If there is something to save then make the request.
		if(data.length > 0){
			this._executeXhrRequest(data);
		}
	}
});