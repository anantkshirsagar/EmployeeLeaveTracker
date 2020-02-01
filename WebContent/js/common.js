function logout(){
	sessionStorage.setItem("email", undefined);
	sessionStorage.setItem("sessionId", undefined);
	window.location= 'index.html';
}

