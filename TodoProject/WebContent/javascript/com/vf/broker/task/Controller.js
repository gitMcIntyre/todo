dojo.provide("vf.broker.task.Controller");

/**
 * This class handles the initialization and cleanup of the view for the Todo List User Interface.
 */
dojo.require("vf.broker.task.View");

dojo.declare("vf.broker.task.Controller", null, {
	
	/**
	 * The view instance.
	 * @type {vf.broker.task.View}
	 */
	_view: null,
	
	constructor: function(){
		this.createViewInstance();
	},
	
	/**
	 * Create a new view instance.
	 */
	createViewInstance: function(){
		this._view = new vf.broker.task.View();
	},
	
	/**
	 * Initialize the view.
	 */
	initialize: function(){
		this.getView().initialize();
	},
	
	/**
	 * Release any resources and call cleanup on the view.
	 */
	cleanup: function(){
		this.getView().cleanup();
		delete this._view;
	},
	
	getView: function(){
		return this._view;
	}
});