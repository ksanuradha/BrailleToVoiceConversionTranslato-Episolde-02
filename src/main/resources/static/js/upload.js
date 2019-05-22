/**
 * 2019-05-22 K S Anuradha
 */
//bind the on-change event
	$(document).ready(function() {
		$("#upload-file-input").on("change", uploadFile);
	});
	/**
	 * Upload the file sending it via Ajax at the Spring Boot server.
	 */
	function uploadFile() {
		$.ajax({
			url : "/api/brailleapplication/uploadFile",
			type : "POST",
			data : new FormData($("#upload-file-form")[0]),
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			cache : false,
			success : function() {
				previewFile();
			},
			error : function() {
			}
		});
	} // function uploadFile 

	function previewFile() {
		var preview = document.querySelector('img'); //selects the query named img
		//var file = document.querySelector("#upload-file-form").[0]; //sames as here
		var file = document.querySelector('input[type=file]').files[0];
		var reader = new FileReader();

		reader.onloadend = function() {
			preview.src = reader.result;
		}

		if (file) {
			reader.readAsDataURL(file); //reads the data as a URL
		} else {
			preview.src = "";
		}
	}