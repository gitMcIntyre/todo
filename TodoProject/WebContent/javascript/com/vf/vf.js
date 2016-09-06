//Register the module patch so dojo knows where our code is located.
if (typeof(dojo) !== "undefined"){
	if (typeof(dojo.config) !== "undefined"){
		dojo.registerModulePath("vf", "../../com/vf");
	}
}

/**
 * This could be used for general functions across the web project.
 */
vf = {
		
};