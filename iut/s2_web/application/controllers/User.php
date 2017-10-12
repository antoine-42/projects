<<<<<<< HEAD
<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class User extends CI_Controller {
	public function date_valid($date){
	    $day = (int) substr($date, 8, 2);
	    $month = (int) substr($date, 5, 2);
	    $year = (int) substr($date, 0, 4);
	    return checkdate($month, $day, $year);
	}

	public function index(){
		$this->load->library('table');
		$this->load->database();
		$this->load->model('model_user');

		$HeaderParam = array();
		$HeaderParam["additionalCSS"] = "user";

		$this->load->view('templates/header', $HeaderParam);
		$this->load->view('user');
		$this->load->view('templates/footer');
	}

	public function connect(){
		$this->load->library(array('table', 'form_validation'));
		$this->load->database();
		$this->load->helper(array('form', 'url'));
		$this->load->model('model_user');

		$this->form_validation->set_rules('username', 'Username', 'required');
		$this->form_validation->set_rules('password', 'Password', 'required');

		if($this->form_validation->run() == TRUE){
			$result = $this->model_user->connect_user(set_value('username'), set_value('password'));
			var_dump($result);
		}
		else {
			redirect('/home/', 'location');
		}
	}

	public function create(){
		$this->load->library(array('table', 'form_validation'));
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
			redirect('/home/', 'location');
		}
	}
}
=======
<?php
defined('BASEPATH') OR exit('No direct script access allowed');

//alert: flashdata

class User extends CI_Controller {
	public function date_valid($date){
	    $day = (int) substr($date, 8, 2);
	    $month = (int) substr($date, 5, 2);
	    $year = (int) substr($date, 0, 4);
	    return checkdate($month, $day, $year);
	}

	public function index(){
		$this->load->library(array('table', 'session'));
		$this->load->database();
		$this->load->model('model_user');

		if(!$this->session->has_userdata('logged_in') || !$this->session->logged_in){
			redirect('home/', 'location'); //send alert: login
		}


		$user_data = $this->model_user->get_user_info($this->session->username);

		$HeaderParam = array();
		$HeaderParam["additionalCSS"] = "user";

		$this->load->view('templates/header', $HeaderParam);
		$this->load->view('user', $user_data);
		$this->load->view('templates/footer');
	}

	public function disconnect(){
		$this->load->library(array('session'));
		$this->load->helper(array('url'));

		$this->session->sess_destroy();

		redirect('home/', 'location'); //send alert: success
	}

	public function connect(){
		$this->load->library(array('table', 'form_validation', 'session'));
		$this->load->database();
		$this->load->helper(array('form', 'url'));
		$this->load->model('model_user');

		$this->form_validation->set_rules('username', 'Username', 'required');
		$this->form_validation->set_rules('password', 'Password', 'required');

		if($this->form_validation->run() == TRUE){
			$result = $this->model_user->connect_user(set_value('username'), set_value('password'));
			
			if($result){
				$data = array(
				        'username'  => set_value('username'),
				        'logged_in' => TRUE
				);
				$this->session->set_userdata($data);

				redirect('user/', 'location');
			}
			else{
				redirect('home/', 'location'); //send alert: bad login
			}
		}
		else {
			redirect('home/', 'location'); //send alert: invalid form
		}
	}

	public function create(){
		$this->load->library(array('table', 'form_validation', 'session'));
		$this->load->database();
		$this->load->helper(array('form', 'url'));
		$this->load->model('model_user');

		$this->form_validation->set_rules('username',   'Username',   'required|is_unique[users.username]');
		$this->form_validation->set_rules('password',   'Password',   'required');
		$this->form_validation->set_rules('email',      'Email',      'required|valid_email|is_unique[users.email]');
		$this->form_validation->set_rules('first_name', 'First Name', 'required');
		$this->form_validation->set_rules('last_name',  'Last Name',  'required');
		$this->form_validation->set_rules('birth_date', 'Birth Date', 'required|callback_date_valid');

		if($this->form_validation->run() == TRUE){
			$result = $this->model_user->create_user(set_value('username'), set_value('password'), set_value('email'), set_value('first_name'), set_value('last_name'), set_value('birth_date'));
			var_dump($result);
		}
		else {
			redirect('home/', 'location'); //send alert: invalid form
		}
	}
}
>>>>>>> 8dec03fc4632d422adc2ca1789b5d5a2dadc8683
