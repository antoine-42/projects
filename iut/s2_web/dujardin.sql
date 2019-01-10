-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 06, 2017 at 03:36 PM
-- Server version: 10.1.18-MariaDB
-- PHP Version: 7.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dujardin`
--

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `name` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `password_required` int(11) NOT NULL DEFAULT '1',
  `confirm_required` int(11) NOT NULL DEFAULT '1',
  `is_secret` int(11) NOT NULL DEFAULT '0',
  `admin` varchar(60) NOT NULL,
  `sport` varchar(60) NOT NULL,
  `location` varchar(60) NOT NULL,
  `mixed` enum('men','both','women') NOT NULL,
  `description` text NOT NULL,
  `members` text NOT NULL,
  `trainers` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 PRIMARY KEY(name);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `email` varchar(254) NOT NULL,
  `first_name` varchar(60) NOT NULL,
  `last_name` varchar(60) NOT NULL,
  `birth_date` date NOT NULL,
  `account_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_admin` int(11) NOT NULL DEFAULT '0',
  `is_banned` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `email`, `first_name`, `last_name`, `birth_date`, `account_creation`, `is_admin`, `is_banned`) VALUES
('antoine42', '42', 'antoinedujardin42@gmail.com', 'antoine', 'dujardin', '1998-04-15', '2017-06-06 15:18:04', 1, 0),
('test', 'test', 'test@gmail.com', 'test', 'test', '2000-01-01', '2017-06-06 15:21:43', 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`name`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
