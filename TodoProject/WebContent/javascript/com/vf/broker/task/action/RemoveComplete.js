dojo.provide("vf.broker.task.action.RemoveComplete");

/**
 * Removes tasks from the store that have a status of 'Complete'.
 */
dojo.declare("vf.broker.task.action.RemoveComplete", null, {
	
	/**
	 * The vf.broker.task.View object.
	 */
	view: null,
	
	constructor: function(attrMap){
		dojo.mixin(this, attrMap);
	},
	
	execute: function(){
		var v = this.view;
		var grid = v.getGrid();
		var store = grid.get("store");
		
		var onComplete = function(items, request){
			dojo.forEach(items, function(item){
				v.deleteItem(item, store);
			});
		};
		store.fetch({query:{status:"Complete"}, onComplete: onComplete});

	}
});