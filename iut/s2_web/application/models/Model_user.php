<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class model_user extends CI_Model
{
    protected $table = "users";


	public function connect_user($username, $password){
        $this->db->select('password');
        $this->db->where('username', $username);
		$query = $this->db->get($this->table);
		$res = $query->result();

		if (sizeof($res) < 1) {
        	return FALSE;
		}

		$pass = $res[0]->password;

        return $pass === $password;
	}

	public function create_user($username, $password, $email, $first_name, $last_name, $birth_date){
		$this->db->set('username',   $username);
		$this->db->set('password',   $password);
		$this->db->set('email',      $email);
		$this->db->set('first_name', $first_name);
		$this->db->set('last_name',  $last_name);
		$this->db->set('birth_date', $birth_date);

        return $this->db->insert($this->table);
	}

	public function edit_user($username, $password = null, $email = null, $first_name = null, $last_name = null, $birth_date = null, $is_banned = null){
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
        return $this->db->update($this->table, $data);*/
	}
	public function ban_user($username){

        return edit_user($username, $is_banned = 1);
	}

	public function get_user_info($username){
		$this->db->select('username, email, first_name, last_name, birth_date, account_creation, is_admin, is_banned');
		$query = $this->db->get($this->table);
		return $query->result_array()[0];
	}
}
