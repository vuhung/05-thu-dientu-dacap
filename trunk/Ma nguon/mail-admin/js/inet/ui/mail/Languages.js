(function(){
	/**
	 * Languages for Website
	 */
	var __languages = [{lang:'vn',resource:'js/ext/resources/ext-lang-vn.js',charset:'UTF-8'},{lang:'en'}];
	var __language = __languages[0];


	// get language parameter.
	var __params = Ext.urlDecode(window.location.search.substring(1));
	if (__params.lang){
		for(var index=0; index < __languages.length; index++){
		    if (__params.lang == __languages[index].lang) __language = __languages[index];
		}
	}

	// get resource and charset.
	var __resource = __language.resource || 'js/ext/resources/ext-lang-vn.js';
	var __charset = __language.charset || 'UTF-8';

	// create script element and append to head tag.
	var __script = document.createElement("script") ;
	__script.type = "text/javascript" ;
	__script.src = __resource;
	__script.charset = __charset;

	// get head element.
	var __heads = document.getElementsByTagName("head");

	// exist head tag.
	if(__heads != null && __heads.length > 0){
		// get first head.
		__heads[0].appendChild(__script);
	}
})();
