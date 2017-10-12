<form action="./" method="post" id="newTeamForm">
	<h2>Creer equipe</h2>
	<div class="formContent">
		<p>Toutes ces valeurs sauf le nom de l'equipe pourront etre changees.</p>
		<div>
			<label for="name"><p>Nom</p></label>
			<?php echo form_error('username'); ?>
			<input type="text" name="name" id="name"/>
		</div>
		<div>
			<?php echo form_error('password_required'); ?>
			<?php echo form_error('password'); ?>
			<label for="password_required">
				<input type="checkbox" name="password_required" value="password_required" id="password_required" checked />
				Mot de passe requis pour entrer dans l'equipe
			</label>
			<input type="password" name="password" id="password"/>
		</div>
		<div>
			<?php echo form_error('confirm_required'); ?>
			<label for="confirm_required">
				<input type="checkbox" name="confirm_required" value="confirm_required" id="confirm_required" checked />
				Confimation requise pour entrer dans l'equipe
			</label>
		</div>
		<div>
			<?php echo form_error('is_secret'); ?>
			<label for="is_secret">
				<input type="checkbox" name="is_secret" value="is_secret" id="is_secret" />
				Equipe secrete
			</label>
		</div>
		<div>
			<label for="default_sport"><p>Sport</p></label>
			<?php echo form_error('default_sport'); ?>
			<select name="default_sport" id="default_sport"/>
				<option value="foot">Foot</option>
				<option value="rugby">Rugby</option>
				<option value="basket">Basket</option>
				<option value="other">Autre</option>
			</select> 
			<?php echo form_error('custom_sport'); ?>
			<input type="text" name="custom_sport" id="custom_sport"/>
		</div>
		<div>
			<label for="location"><p>Location</p></label>
			<?php echo form_error('location'); ?>
			<input type="text" name="location" id="location"/>
		</div>
		<div>
			<label for="max_users"><p>Nombre d'utilisateur maximal</p></label>
			<?php echo form_error('max_users'); ?>
			<input type="number" name="max_users" id="max_users" min="1"/>
		</div>
		<div>
			<label for="mixite"><p>Mixite</p></label>
			<?php echo form_error('mixite'); ?>
			<select name="mixite" id="mixite"/>
				<option value="both" checked>Peu importe</option>
				<option value="men">Seulement garcons</option>
				<option value="women">Seulement filles</option>
			</select> 
		</div>
		<div>
			<label for="description"><p>Description</p></label>
			<?php echo form_error('description'); ?>
			<textarea name="description" id="description" rows="5"></textarea>
		</div><!--
		<div>
			<label for="logo"><p>Logo</p></label>
			<input type="hidden" name="MAX_FILE_SIZE" value="1000000"/>
			<input type="file" accept="image/*" name="logo" id="logo"/>
		</div>-->
		<input type="submit" value="OK">
	</div>
</form>
<script src="http://dwarves.arda/~dujardin/wim_projet_2017/assets/js/new_team.js"></script>