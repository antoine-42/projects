<<<<<<< HEAD
<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Team extends CI_Controller {

	public function index(){
		$this->load->library('table');

		$this->load->view('templates/header');
		$this->load->view('team');
		$this->load->view('templates/footer');
	}

	public function create(){
		$this->load->library('table');

		$HeaderParam = array();
		$HeaderParam["additionalCSS"] = "new_team";
		$HeaderParam["additionalJS"] = "new_team";

		$this->load->view('templates/header', $HeaderParam);
		$this->load->view('new_team');
		$this->load->view('templates/footer');
	}
}
=======
<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Team extends CI_Controller {

	public function index(){
		$this->load->library(array('table', 'session'));

		$this->load->view('templates/header');
		$this->load->view('team');
		$this->load->view('templates/footer');
	}
/*
	public function create(){
		$this->load->library(array('table', 'form_validation', 'session'));
		$this->load->database();
		$this->load->helper(array('form', 'url'));
		$this->load->model('model_user');

		$this->form_validation->set_rules('username', 'Username', 'required|is_unique[users.username]');
		$this->form_validation->set_rules('password', 'Password', 'required');
		$this->form_validation->set_rules('email', 'Email', 'required|valid_email|is_unique[users.email]');
		$this->form_validation->set_rules('first_name', 'First Name', 'required');
		$this->form_validation->set_rules('last_name', 'Last Name', 'required');
		$this->form_validation->set_rules('birth_date', 'Birth Date', 'required|callback_date_valid');

		if($this->form_validation->run() == TRUE){
			$result = $this->model_user->create_user(set_value('username'), set_value('password'), set_value('email'), set_value('first_name'), set_value('last_name'), set_value('birth_date'));
			var_dump($result);
		}
		else {
			redirect('home/', 'location'); //send alert: invalid form
		}
	}*/
	public function create(){
		$this->load->library(array('table', 'form_validation', 'session'));
		$this->load->database();
		$this->load->helper(array('form', 'url'));
		$this->load->model('model_team');


		$this->form_validation->set_rules('name',              'Name',                          'required|is_unique[teams.name]');
		$this->form_validation->set_rules('password_required', 'Mot de passe requis',           'required');
		$this->form_validation->set_rules('confirm_required',  'Confirmation requise',          'required');
		$this->form_validation->set_rules('is_secret',         'Equipe secrete',                'required');
		$this->form_validation->set_rules('default_sport',     'Sport predefini',               'required');
		$this->form_validation->set_rules('max_users',         'Nombre d\'utilisateur maximal', 'required|is_natural_no_zero');
		$this->form_validation->set_rules('mixite',            'Mixite',                        'required');
		$this->form_validation->set_rules('description',       'Description',                   'required');

		$this->form_validation->set_message('required', '{field} doit etre rempli');
		$this->form_validation->set_message('is_unique', '{param} est deja pris');
		$this->form_validation->set_message('is_natural_no_zero', '{field} doit etre un nombre superieur a 0');

		if($this->input->post('password_required')){
			$this->form_validation->set_rules('password', 'Mot de passe', 'required');
		}
		if($this->input->post('default_sport') == "other"){
			$this->form_validation->set_rules('custom_sport', 'Sport custom', 'required');
		}
name, $password, $password_required, $confirm_required, $is_secret, $admin, $sport, $location, $max_users, $mixed, $description

		if($this->form_validation->run() == TRUE){
			$result = $this->model_team->create_team(set_value('name'), set_value('password_required'), set_value('password'), set_value('is_secret'), set_value('default_sport'), set_value('location'), set_value('max_users'), set_value('mixite'), set_value('description'));
			var_dump($result);
		}
		else {
			//send alert: invalid form
		}


		$HeaderParam = array();
		$HeaderParam["additionalCSS"] = "new_team";
		$HeaderParam["additionalJS"] = "new_team";

		$this->load->view('templates/header', $HeaderParam);
		$this->load->view('new_team');
		$this->load->view('templates/footer');
	}
}
>>>>>>> 8dec03fc4632d422adc2ca1789b5d5a2dadc8683
