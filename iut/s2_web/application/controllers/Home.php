<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Home extends CI_Controller {

	public function index(){
		$this->load->library(array('table', 'session'));

		if($this->session->has_userdata('logged_in') && $this->session->logged_in){
			redirect('user/', 'location');
		}


		$HeaderParam = array();
		$HeaderParam["additionalCSS"] = "home";

		$this->load->view('templates/header', $HeaderParam);
		$this->load->view('home');
		$this->load->view('templates/footer');
	}
}
