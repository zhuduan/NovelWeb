	
	function loadOnlineMonitor(){
		var userID = $("#userID").val();
		window.location.href = "/commonService/onlineMonitor/all/" + userID;
	}
	
	function loadFollow(){
		var userID = $("#userID").val();
		window.location.href = "/commonService/follow/all/" + userID;
	}
	
	function loadUserAccountInfo(userType){
		var userID = $("#userID").val();
		window.location.href = "/common/user/ownAccounts/" + userID +"/" +userType;
	}
	function loadInfoByPage(desUrl,page,size){
		desUrl = desUrl + "?page=" + page + "&size=" + size;
		window.location.href = desUrl;
	}