<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class model_team extends CI_Model
{
    protected $table = "teams";


	public function create_team($name, $password, $password_required, $confirm_required, $is_secret, $admin, $sport, $location, $max_users, $mixed, $description){
		$this->db->set('name',              $name);
		$this->db->set('password',          $password);
		$this->db->set('password_required', $password_required);
		$this->db->set('confirm_required',  $confirm_required);
		$this->db->set('is_secret',         $is_secret);
		$this->db->set('admin',             $admin);
        $this->db->set('sport',             $sport);
        $this->db->set('location',          $location);
        $this->db->set('max_users',         $max_users);
        $this->db->set('mixed',             $mixed);
        $this->db->set('description',       $description);

        return $this->db->insert($this->table);
	}

	public function edit_team($name, $password = null, $password_required = null, $confirm_required = null, $is_secret = null, $admin = null, $sport = null, $location = null, $mixed = null, $description = null, $members = null, $trainers = null){
/*
        $data = array();
        if($password != null){
        	array_push($data, 'password' => $password);
        }
        if($email != null){
        	array_push($data, 'email' => $email);
        }
        if($first_name != null){
        	array_push($data, 'first_name' => $first_name);
        }
        if($last_name != null){
        	array_push($data, 'last_name' => $last_name);
        }
        if($birth_date != null){
        	array_push($data, 'birth_date' => $birth_date);
        }
        $this->db->where('username', $username);
        $this->db->update($this->table, $data);*/
	}

	public function get_team_info($name){
		$this->db->select('name, password_required, confirm_required, is_secret, admin, sport, location, mixed, description, members, trainers');
		$query = $this->db->get($this->table);
		return $query->result_array()[0];
	}
}
