dojo.provide("vf.broker.task.View");

/**
 * The task view consists of a data grid that provides a list of todo tasks. 
 * - A user can add, edit, and delete tasks. 
 * - The status of each task can be toggled between active and complete.
 * - Multiple task can be selected and deleted at once.
 * - Completed, Active, or all tasks can be viewed.
 * - All Complete tasks can be deleted
 * 
 * Note: data changes will be persisted when a Save is executed.
 */

dojo.require("vf.broker.task.action.Save");
dojo.require("vf.broker.task.action.RemoveComplete");

dojo.require("dojox.grid.EnhancedGrid");
dojo.require("dijit.form.Button");
dojo.require("dojox.uuid.generateRandomUuid");
dojo.require("dojo.data.ItemFileWriteStore");

dojo.declare("vf.broker.task.View", null, {
	
	/**
	 * Keep track of connections so we can release them during cleanup.
	 */
	_connects: [],
	
	/**
	 * Keep track of deleted items that have not been persisted.
	 */
	_deletedItems: [],
	
	/**
	 * Initialize the UI.
	 */
	initialize: function(){
		this.setGridStore();
		this._connectEvents();
	},
	
	/**
	 * Set the store connected to the Grid. This will cause an automatic fetch of the data.
	 */
	setGridStore: function(){
		var data = {
			identifier: 'rowId',
			label: 'id',
			items: []
		};

		//Set the url to the server component to load the grid.
		var url = this.getApplicationPath(window.location) + "/home/fetch.htm";
		var store = new dojo.data.ItemFileWriteStore({data: data, url: url});

		this.getGrid().setStore(store);
		this._deletedItems = []; //Reset the deleted items array.
	},
	
	/**
	 * Updates the displayed number of active tasks.
	 */
	_setActiveCount: function(){
		var g = this.getGrid();
		var s = g.store;
		var activeCount = 0;
		s.fetch({query:{status:'Active'}, onComplete: function(items){
			activeCount = items.length;
		}});
		var cNode = dojo.byId("countNode");
		cNode.innerText = activeCount;
	},
	
	/**
	 * Returns the URL for the application useing the window location.
	 */
	getApplicationPath: function(/*window.location*/winLocation){
		var protocol = winLocation.protocol;
		var host = winLocation.host;
		var pathname = winLocation.pathname;
		var appPath = "";
		
		var parts = pathname.split("/");
		for(var part in parts){
			if(dojo.isString(parts[part]) && parts[part] !== ""){
				appPath = parts[part];
				break;
			}
		}
		
		var path = protocol + "//" + host + "/" + appPath;

		return path;
	},

	/**
	 * Connect the UI event handlers.
	 */
	_connectEvents: function(){
		var hitchCreate = dojo.hitch(this, this.handleCreate);
		var handleCreate = dojo.connect(this.getCreateButton(), "onClick", hitchCreate);
		
		var hitchSave = dojo.hitch(this, this.handleSave);
		var handleSave = dojo.connect(this.getSaveButton(), "onClick", hitchSave);
		
		var hitchRemove = dojo.hitch(this, this.handleRemove);
		var handleRemove = dojo.connect(this.getRemoveButton(), "onClick", hitchRemove);

		var hitchRemoveCompleted = dojo.hitch(this, this.handleRemoveCompleted);
		var handleRemoveCompleted = dojo.connect(this.getRemoveCompletedButton(), "onClick", hitchRemoveCompleted);
		
		var hitchViewComplete = dojo.hitch(this, this.handleViewComplete);
		var handleViewComplete = dojo.connect(this.getViewComplete(), "onClick", hitchViewComplete);
		
		var hitchViewAll = dojo.hitch(this, this.handleViewAll);
		var handleViewAll = dojo.connect(this.getViewAll(), "onClick", hitchViewAll);
		
		var hitchViewActive = dojo.hitch(this, this.handleViewActive);
		var handleViewActive = dojo.connect(this.getViewActive(), "onClick", hitchViewActive);
		
		var hitchGrid = dojo.hitch(this, this.handleStyleRow);
		var handleGridStyle = dojo.connect(this.getGrid(), "onStyleRow", hitchGrid);
		
		this._connects.push([handleCreate, handleSave, handleRemove, handleRemoveCompleted, 
		                     handleViewComplete, handleViewAll, handleViewActive, handleGridStyle]);
	},
	
	getViewActive: function(){
		return dijit.byId("viewActive");
	},
	
	getViewAll: function(){
		return dijit.byId("viewAll");
	},
	
	getViewComplete: function(){
		return dijit.byId("viewComplete");
	},
	
	getCreateButton: function(){
		return dijit.byId("createButton");
	},
	
	getSaveButton: function(){
		return dijit.byId("saveButton");
	},
	
	getRemoveButton: function(){
		return dijit.byId("removeButton");
	},
	
	getRemoveCompletedButton: function(){
		return dijit.byId("removeCompletedButton");
	},
	
	/**
	 * Return the grid widget.
	 * @return {dojox.grid.EnhancedGrid}
	 */
	getGrid: function(){
		return dijit.byId("grid");
	},
	
	/**
	 * Release resources.
	 */
	cleanup: function(){
		dojo.forEach(this._connects, function(handle){
			dojo.disconnect(handle);
		});
		delete this._deletedItems;
	},
	
	/**
	 * Return the object based on the item from the store.
	 */
	dump: function(item, store){
		
		var newItem = {};
		var attrNames = store.getAttributes(item);

		for(var j = 0; j < attrNames.length; j=j+1){
			newItem[attrNames[j]] = store.getValue(item, attrNames[j]);
		}
		return newItem;
	},

	/***** Event Handlers *****/
	handleStyleRow: function(row){
		var g = this.getGrid();
		if(g){
			var item = g.getItem(row.index);
			if(item){
				var status = g.store.getValue(item, "status");
				if(status === "Active"){
					row.customClasses += " vfactiveRow";
				}
			}
		}
		
		this._setActiveCount();
	},
	
	handleCreate: function(){
		try{
			var grid = this.getGrid();
			var store = grid.get("store");
			
			//This will be reassigned by the save process. We need to guarantee a unique id for new items.
			var newId = dojox.uuid.generateRandomUuid();
			var newItem = {rowId: newId, taskId:0, status: 'Active', description: '', createDate: null};
			store.newItem(newItem);
		}
		catch(ex){
			alert("Create Error");
		}
	},
	
	handleSave: function(){
		var action = new vf.broker.task.action.Save(this);
		action.execute();		
	},
	
	deleteItem: function(item, store){
		if(item){
			var taskId;
			
			if(item !== null){
				taskId = store.getValue(item, "taskId");
				if(taskId !== 0){ //If the task has been previously saved then track the delete.
					store.setValue(item, "delete", true);
					this._deletedItems.push(this.dump(item, store));
				}
				store.deleteItem(item);
			}
		}
	},
	
	handleRemove: function(){
		var grid = this.getGrid();
		var s = grid.get("store");
		
		var items = grid.selection.getSelected();
		if(items.length){
			dojo.forEach(items, function(item){
				this.deleteItem(item, s);
			}, this);
		}
	},
	
	handleRemoveCompleted: function(){
		var action = new vf.broker.task.action.RemoveComplete({view: this});
		action.execute();
	},
	
	filterByStatus: function(status){
		var g = this.getGrid();
		g.filter({status: status}, true);
	},
	
	handleViewComplete: function(){
		this.filterByStatus("Complete");
	},

	handleViewAll: function(){
		this.filterByStatus("*");
	},

	handleViewActive: function(){
		this.filterByStatus("Active");
	}
	
});