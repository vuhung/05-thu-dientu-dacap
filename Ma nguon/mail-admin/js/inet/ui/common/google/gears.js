/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/
// 
// Copyright 2007 Google Inc. All Rights Reversed.
//
// setting up Google Gears to improve the client performance.
(function(){
	// we are already define Google gears.
	if(window.google && google.gears){
		return;
	}	
	
	// create google gears factory.
	var factory = null ;
	
	// we are running under firefox.
	if(typeof GearsFactory !== 'undefined'){
		factory = new GearsFactory() ;
	}else{
		// running under IE.
		try{
			factory = new ActiveXObject('Gears.Factory') ;
		}catch(e){
			// running under Safari
			if(navigator.mimeTypes["application/x-googlegears"]){
				factory = document.createElement("object") ;
				factory.style.display = "none" ;
				factory.width = 0;
				factory.height = 0;
				factory.type = "application/x-googlegears" ;
				document.documentElement.appendChild(factory) ;
			}
		}
	}
	
	// Google Gears is not installed.
	if(!factory) return;
	
	if(!window.google){
		window.google = {} ;
	}
	
	if(!google.gears){
		google.gears = {factory: factory} ;
	}
})();

// setting up the gears.
//if(!window.google || !google.gears){
//	location.href = "http://gears.google.com/?action=install&message=Google%20Gears%20is%20required%20for%20this%20application"
//				  + "&return="+window.location.href ;
//}
