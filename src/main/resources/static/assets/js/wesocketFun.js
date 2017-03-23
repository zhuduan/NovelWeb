	
	
	
		//--- change the show nums
		function statisAccountsNum(){			
			var receiveAccountsNum = $("#receiveTable").find("tr").size() - 1;
			var sendAccountsNum = $("#sendTable").find("tr").size() - 1;
			var unknowAccountsNum = $("#unknowTable").find("tr").size() - 1;
			if(receiveAccountsNum<0){
				receiveAccountsNum = 0;
			}
			if(sendAccountsNum<0){
				sendAccountsNum = 0;
			}
			if(unknowAccountsNum<0){
				unknowAccountsNum = 0;
			}
			var totalAccountsNum = receiveAccountsNum + sendAccountsNum + unknowAccountsNum;
			$("#totalAccountsNum").html(totalAccountsNum);
			$("#receiveAccountsNum").html(receiveAccountsNum);
			$("#sendAccountsNum").html(sendAccountsNum);
			$("#unknowAccountsNum").html(unknowAccountsNum);
		}
		
		//--- websocket info
		var stompClient = null; 
		var userID = 0;
		var lastSendDate = new Date();	
		var clockTimer = self.setInterval("clock()",10000); 		
		function setCurrentUserID(){
			userID = $("#userID").val();		
		}		
		function connect() {        
			var socket = new SockJS('/websocket');        
			stompClient = Stomp.over(socket);        
			stompClient.heartbeat.outgoing = 25000; 	// client will send heartbeats every 30000ms
			stompClient.heartbeat.incoming = 0;         // client does not want to receive heartbeats from the server
			stompClient.connect({}, function (frame) {            
				 sendInfo(userID); 	//tell the server add a new observer				 
				 listenInfo();				
			}, function (message){
				alert("监控程序已断开，如需使用请刷新页面");
			});    
		}  
		function clock(){
			var currentDate = new Date();
			if((currentDate.getTime()-lastSendDate.getTime())>=120000){		//100s is the lost max
				alert("由于没有账号连接到服务器，已断开在线监控服务，请有账号连接后刷新本页面来激活本功能，谢谢");
				self.clearInterval(clockTimer);
			}
		}
		// 断开socket连接
		function disconnect() {        
			if (stompClient != null) {       
				var handleUrl = "/app/commonService/onlineMonitor/" + userID +"/delete";
				stompClient.send(handleUrl, {}, userID); 
				stompClient.disconnect();        
			}        
			setConnected(false);        
			console.log("Disconnected");    
		}    
		// 向‘/app/commonService/onlineMonitor/{userID}’服务端发送消息
		function sendInfo(value) { 
			var handleUrl = "/app/commonService/onlineMonitor/" + userID;
			stompClient.send(handleUrl, {}, value);    
		}    
		// 监听‘/queue/commonService/onlineMonitor/{userID}’的消息
		function listenInfo(){
			var listenUrl = "/queue/commonService/onlineMonitor/" + userID;
			stompClient.subscribe(listenUrl, function (data) {  
				var adjustData = data.body.toString().substr(1,data.body.toString().length-2).replace(/\\/g,"");
				setChangedAccount(adjustData);
				
				var currentDate = new Date();
				if((currentDate.getTime()-lastSendDate.getTime())>=50000){		//50s send once(server expire is 100s)
					sendInfo(userID);
					lastSendDate = new Date();
				}
			});    
		}
		function getShowInfo(status){
			switch(status){
				case -1:
					return "<span>未开通</span>";
				case 0:
					return "<span class=\"am-text-warning\">状态未知</span>";
				case 1:
					return "<span class=\"am-text-secondary\">鉴权通过</span>";
				case 2:
					return "<span class=\"am-text-success\">正常连接</span>";
				case 3:
					return "<span class=\"am-text-success\">正在更新</span>";
				case 4:
					return "<span class=\"am-text-danger\">交易商掉线</span>";
				case 5:
					return "<span class=\"am-text-secondary\">反馈</span>";
				case 6:
					return "<span class=\"am-text-secondary\">无订单</span>";	
				case 7:
					return "<span class=\"am-text-danger\">丢失心跳</span>";
				default:
					return "<span lass=\"am-text-danger\">未知服务器错误</span>";
			}
		}
		function getNewTr(accountID,accountStatus,accountType,balance,lots){
			var result = "<tr id=\"" + accountID + "_" + accountType + "\">" 
							+ "<td class=\"am-text-center\">*</td>"
							+ "<td>" + accountID + "</td>"
							+ "<td>" + getShowInfo(accountStatus) + "</td>"
							+ "<td>" + balance + "</td>"
							+ "<td>" + lots + "</td>"
							+ "<td style=\"display:none\">" + accountType + "</td>"
							+ "</tr>";
			return result;
		}
		function setChangedAccount(ouputJson){
			var jsonData = $.parseJSON(ouputJson);
			var accountID = jsonData.accountID;
			var accountStatus = jsonData.accountStatus;
			var accountType = jsonData.accountType;
			var balance = jsonData.balance;		
			var lots = jsonData.lots;
			var tagetTR = "#"+accountID+"_"+accountType;
			var unknownTR = "#"+accountID+"_"+0;		//unknown

			if(accountType>0){
				if($(unknownTR).size()>0){
					$(unknownTR).remove();
				}
				if($(tagetTR).size()==0){
					switch(accountType){
						case 1:
							$("#sendTable").find("tbody").append(getNewTr(accountID,accountStatus,accountType,balance,lots));		
							break;
						case 2:
							$("#receiveTable").find("tbody").append(getNewTr(accountID,accountStatus,accountType,balance,lots));	
							break;
						default:
							break;
					}
				} else {
					$(tagetTR).find("span").parent().html(getShowInfo(accountStatus));
					$(tagetTR).find("td:eq(3)").html(balance);
					$(tagetTR).find("td:eq(4)").html(lots);
				}
			}			
			statisAccountsNum();
		}		