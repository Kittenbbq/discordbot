create table commands(
	command VARCHAR(30) PRIMARY KEY,
 	response VARCHAR(1000) NOT NULL,
 	user VARCHAR(50) DEFAULT "USER MISSING",
	added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB;


create table messages(
    ID VARCHAR(30) PRIMARY KEY,
	authorID VARCHAR(30) NOT NULL,
	authorName VARCHAR(30) NOT NULL,
 	sent DATETIME NOT NULL,
 	guildID VARCHAR(30) NOT NULL,
	guildName VARCHAR(50) NOT NULL,
	channelID VARCHAR(30) NOT NULL,
	channelName VARCHAR(50)NOT NULL,
	content VARCHAR(2000)
)ENGINE=InnoDB;


-- PROCEDURES --

CREATE DEFINER=`root`@`localhost` PROCEDURE `messageInfo`()
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
SELECT
	COUNT(*) as totalMessages,
	MIN(sent) as firstMessage,
	MAX(sent) as lastMessage
FROM messages


CREATE DEFINER=`root`@`localhost` PROCEDURE `messageCountByAuthor`(
	IN `fromDate` VARCHAR(50),
	IN `toDate` VARCHAR(50)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINERDay
COMMENT ''
BEGIN
SELECT authorID, authorName, COUNT(*) messageCount from messages
WHERE date(sent) >= fromDate AND date(sent) <= toDate
GROUP BY authorname
ORDER BY messageCount DESC;
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `messageCountByDate`(
	IN `fromDate` VARCHAR(50),
	IN `toDate` VARCHAR(50)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
	SELECT date(sent) as date, COUNT(*) as messageCount FROM messages
	WHERE date(sent) >= fromDate AND date(sent) <= toDate
	GROUP BY date(sent);
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `messageCountByDay`(
	IN `fromDate` VARCHAR(50),
	IN `toDate` VARCHAR(50)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
SELECT dayname(sent) as day, hour(sent) as hour, COUNT(*) as messageCount FROM messages
WHERE date(sent) >= fromDate AND date(sent) <= toDate
GROUP BY day;
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `messageCountByDayHour`(
	IN `fromDate` VARCHAR(50),
	IN `toDate` VARCHAR(50)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
SELECT dayname(sent) as day, hour(sent) as hour, COUNT(*) as messageCount FROM messages
WHERE date(sent) >= fromDate AND date(sent) <= toDate
GROUP BY day, hour;
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `messageCountByHour`(
	IN `fromDate` VARCHAR(50),
	IN `toDate` VARCHAR(50)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
	SELECT HOUR(sent) as hour, COUNT(*) as messageCount FROM messages
	WHERE date(sent) >= fromDate AND date(sent) <= toDate
	GROUP BY HOUR(sent);
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `messagesWithUrl`(
	IN `fromDate` VARCHAR(50),
	IN `toDate` VARCHAR(50)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
SELECT content FROM messages
WHERE
	 (date(sent) >= fromDate AND date(sent) <= toDate)
	 AND
	 (content LIKE "%http://%" OR content LIKE "%https://%");
END