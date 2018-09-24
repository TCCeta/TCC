<!--Função do preview da imagem-->
				function readURL(input) {

					if (input.files && input.files[0]) {
						var reader = new FileReader();

						reader.onload = function(e) {
							$('#imagemItem').attr('src', e.target.result);
						};

						reader.readAsDataURL(input.files[0]);

					}
				};
