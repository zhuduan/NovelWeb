		function loadHref(targetPage,desUrl){			
			var dataSize = $("#dataSize").html();			
			loadInfoByPage(desUrl,targetPage,dataSize);
		}
		
		function getPageInfo(desUrl){
			var dataSize = parseInt($("#dataSize").html());
			var totalNums = parseInt($("#totalNum").html());
			var currentPage = parseInt($("#requestPage").val());
			var maxPage = Math.floor(totalNums/dataSize);	//向下取整（因为首页从0开始）
			var minPage = 0;		//page从0开始
			
			var firstPage = "";
			var pageShow1 = "";
			var pageShow2 = "";
			var pageShow3 = "<li class='am-active'><a href='#'>"+(currentPage+1)+"</a></li>";
			var pageShow4 = "";
			var pageShow5 = "";
			var lastPage = "";
			
			$("#pageUL").empty();
			if(currentPage<=minPage){
				firstPage = "<li class='am-disabled'><a href='#'>«</a></li>";
			} else{
				firstPage = "<li><a href='#' onclick='loadHref("+minPage+",\""+desUrl+"\")'>«</a></li>";
			}
			$("#pageUL").append(firstPage);
			
			var pageM2 = currentPage-2;
			if(pageM2<minPage){
				pageShow1 = "";
			} else{
				pageShow1 = "<li><a href='#' onclick='loadHref("+pageM2+",\""+desUrl+"\")'>"+(pageM2+1)+"</a></li>";
				$("#pageUL").append(pageShow1);
			}
			
			var pageM1 = currentPage-1;
			if(pageM1<minPage){
				pageShow2 = "";
			} else{
				pageShow2 = "<li><a href='#' onclick='loadHref("+pageM1+",\""+desUrl+"\")'>"+(pageM1+1)+"</a></li>";
				$("#pageUL").append(pageShow2);
			}
			
			$("#pageUL").append(pageShow3);
			
			var pageP1=currentPage+1;
			if(pageP1>maxPage){
				pageShow4 = "";
			} else{
				pageShow4 = "<li><a href='#' onclick='loadHref("+pageP1+",\""+desUrl+"\")'>"+(pageP1+1)+"</a></li>";
				$("#pageUL").append(pageShow4);
			}
			
			var pageP2=currentPage+2;
			if(pageP2>maxPage){
				pageShow5 = "";
			} else{
				pageShow5 = "<li><a href='#' onclick='loadHref("+pageP2+",\""+desUrl+"\")'>"+(pageP2+1)+"</a></li>";
				$("#pageUL").append(pageShow5);
			}
			
			if(currentPage>=maxPage){
				lastPage = "<li class='am-disabled'><a href='#'>»</a></li>";
			} else{
				lastPage = "<li><a href='#' onclick='loadHref("+maxPage+",\""+desUrl+"\")'>»</a></li>";
			}
			$("#pageUL").append(lastPage);	
		}