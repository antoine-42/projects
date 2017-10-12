<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8" />
		<title>Gestion d'equipes sportives</title>
		<link rel="stylesheet" href="<?php echo base_url();?>assets/css/style.css">
		<?php
		if(isset($additionalCSS)){
			echo "<link rel=\"stylesheet\" href=\"" . base_url() . "assets/css/" . $additionalCSS . ".css\">";
		}
		if(isset($additionalJS)){
			echo "<script src=\"" . base_url() . "assets/js/" . $additionalJS . ".js\"></script>";
		}
		?>
	</head>
	<body container>
		<h1 class="_bb1 _mbxs">Gestion d'equipes sportives</h1>
