-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 03, 2026 at 07:59 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `theater`
--

-- --------------------------------------------------------

--
-- Table structure for table `actions`
--

CREATE TABLE `actions` (
  `actionId` int(11) NOT NULL,
  `actionUser` varchar(256) NOT NULL,
  `actionTaken` varchar(256) NOT NULL,
  `actionDate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `actions`
--

INSERT INTO `actions` (`actionId`, `actionUser`, `actionTaken`, `actionDate`) VALUES
(1, 'Manager', ' Movie has been Added: The Lord Of The Rings', '2026-04-03 19:48:59'),
(2, 'manager', 'Account locked after 3 attempts', '2026-04-03 20:36:37');

-- --------------------------------------------------------

--
-- Table structure for table `maintenancereport`
--

CREATE TABLE `maintenancereport` (
  `reportId` int(11) NOT NULL,
  `equipment` varchar(256) NOT NULL,
  `description` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `maintenancereport`
--

INSERT INTO `maintenancereport` (`reportId`, `equipment`, `description`) VALUES
(1, 'headsets', 'the headset at this location is broken'),
(2, 'Speaker', 'The speakers in room 4 are Broken and in need of immediate fix.'),
(3, 'screan', 'the screans are broken.');

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

CREATE TABLE `movies` (
  `movieId` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `director` varchar(32) NOT NULL,
  `duration` int(11) NOT NULL,
  `description` varchar(256) NOT NULL,
  `genre` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`movieId`, `name`, `director`, `duration`, `description`, `genre`) VALUES
(1, 'Inception', 'Christopher Nolan', 1, 'A skilled thief enters people\'s dreams to steal secrets but is tasked with planting an idea instead', 'sci-fi'),
(2, 'The Shawshank Redemption', 'Frank Darabont', 2, 'A banker sentenced to life in prison forms a friendship and finds hope while enduring harsh conditions', 'Drama'),
(3, 'Parasite', 'Bong Joon-ho', 2, 'A poor family schemes to work for a wealthy household, leading to unexpected and dark consequences', 'Thriller'),
(4, 'The Dark Knight', 'Christopher Nolan', 2, 'Batman faces the Joker, a chaotic criminal mastermind who pushes Gotham to the brink', 'Action'),
(5, 'Forrest Gump', 'Robert Zemeckis', 2, 'A simple man with a kind heart unintentionally influences major historical events in the U.S', 'Drama'),
(16, 'The Lord Of The Rings', 'Peter Jackson', 2, 'The Lord of the Rings is a trilogy of epic fantasy films directed by Peter Jackson. The films are based on the novel The Lord of the Rings by J. R. R. Tolkien, and are titled identically to the three volumes of the novel.', 'Fantasy');

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `paymentId` int(11) NOT NULL,
  `ticketId` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `method` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`paymentId`, `ticketId`, `amount`, `method`) VALUES
(1, 8, 80, 'cash'),
(2, 9, 80, 'cash'),
(3, 10, 80, 'Cash'),
(4, 11, 80, 'Credit Card'),
(5, 12, 80, 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `roomId` int(11) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`roomId`, `capacity`) VALUES
(1, 10),
(2, 10),
(3, 10),
(4, 10),
(5, 10),
(6, 10);

-- --------------------------------------------------------

--
-- Table structure for table `showtime`
--

CREATE TABLE `showtime` (
  `showtimeId` int(11) NOT NULL,
  `movieId` int(11) NOT NULL,
  `room` int(11) NOT NULL,
  `date` date NOT NULL,
  `timeSlot` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `showtime`
--

INSERT INTO `showtime` (`showtimeId`, `movieId`, `room`, `date`, `timeSlot`) VALUES
(5, 3, 2, '2026-03-01', 4),
(8, 2, 6, '2026-03-18', 5);

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

CREATE TABLE `ticket` (
  `ticketId` int(11) NOT NULL,
  `showtimeId` int(11) NOT NULL,
  `seat` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ticket`
--

INSERT INTO `ticket` (`ticketId`, `showtimeId`, `seat`, `price`) VALUES
(2, 5, 3, 80),
(3, 5, 8, 80),
(4, 5, 9, 80),
(5, 8, 4, 80),
(6, 5, 7, 80),
(7, 5, 6, 80),
(8, 5, 1, 80),
(9, 8, 10, 80),
(10, 8, 1, 80),
(11, 5, 10, 80),
(12, 5, 2, 80);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(256) NOT NULL,
  `role` varchar(32) NOT NULL,
  `failedAttempts` int(11) NOT NULL DEFAULT 0,
  `isLocked` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `username`, `password`, `role`, `failedAttempts`, `isLocked`) VALUES
(1, 'Manager', '$2a$12$427bzvuT1EVbkmgVBQ2VGeGqNSVspuEweJy52nA9mkN.hZKS5oADi', 'MANAGER', 0, 0),
(2, 'Staff', '$2a$12$KX57BsBxly9E.eZdKwQBMe4TjkIcWDk8H.YbHCHB7o1g234ciBux6', 'STAFF', 0, 0),
(3, 'Maintenance', '$2a$12$zt354niswjMINPE3gmog6uHf2zu2pmMFAZHTpB87bX7bLHE69k8pu', 'MAINTENANCE', 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `actions`
--
ALTER TABLE `actions`
  ADD PRIMARY KEY (`actionId`);

--
-- Indexes for table `maintenancereport`
--
ALTER TABLE `maintenancereport`
  ADD PRIMARY KEY (`reportId`);

--
-- Indexes for table `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`movieId`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`paymentId`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`roomId`);

--
-- Indexes for table `showtime`
--
ALTER TABLE `showtime`
  ADD PRIMARY KEY (`showtimeId`);

--
-- Indexes for table `ticket`
--
ALTER TABLE `ticket`
  ADD PRIMARY KEY (`ticketId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `actions`
--
ALTER TABLE `actions`
  MODIFY `actionId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `maintenancereport`
--
ALTER TABLE `maintenancereport`
  MODIFY `reportId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `movies`
--
ALTER TABLE `movies`
  MODIFY `movieId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `paymentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `roomId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `showtime`
--
ALTER TABLE `showtime`
  MODIFY `showtimeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `ticket`
--
ALTER TABLE `ticket`
  MODIFY `ticketId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
