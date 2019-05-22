/**
 * K S Anuradha 2019/05/21
 */
$(document).ready(function() {	
	
	$("#btnContactUs").click(function() {
		scanImage();
	});
	
	function scanImage() {
		$.ajax({
			url : "/api/brailleapplication/scan",
			type : "POST",
			cache : false,
			success : function() {
				alert("K Sasindu Anuradha");
			},
			error : function() {
			}
		});
	} // function uploadFile 

});