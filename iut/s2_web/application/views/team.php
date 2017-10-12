<h1>Gestion Equipe</h1>
<?php
if(isset($_GET['action'])){
	if ($_GET['action'] == "new") {
		echo "<p>New</p>";
	}
	elseif ($_GET['action'] == "gestion") {
		echo "<p>Gestion</p>";
	}
}
?>