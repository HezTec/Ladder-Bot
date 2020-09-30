package coolGroup.LadderBot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import sqlClasses.ChallengerUpdate;
import sqlClasses.CreateLadderDB;
import sqlClasses.LadderCheck;
import sqlClasses.LadderInsert;
import java.io.File;

import commands.AdminCommand;
// this is the old challenge commands import commands.ChallengeCommand;
import commands.ChallengeCommandGlicko;
import commands.LadderInfoCommand;

public class App extends ListenerAdapter {
	
	public static void main(String[] args) throws Exception {
		
		File jarFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		File file = new File(jarFile.getParentFile().getParent() + "\\LADDER.db");
		System.out.println(file);

		if (!file.exists() && !file.isDirectory()) {
			CreateLadderDB c = new CreateLadderDB();
			c.create();
		}
		
		// resetting all challenger id's in the database to none
		ChallengerUpdate resetAll = new ChallengerUpdate();
		resetAll.resetAll();

		JDA jda = new JDABuilder(AccountType.BOT).setToken(Ref.token).build().awaitReady();
		jda.addEventListener(new App());

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent evt) {
		Message objMsg = evt.getMessage();

		// if user is a boot it does nothing
		if (evt.getAuthor().isBot()) {
			return;
		} else 
		// scans to see if the first character in the string is the prefix
		if(objMsg.getContentRaw().substring(0,1).equals(Ref.prefix)) {
		
		// objects
		User objUser = evt.getAuthor();
		MessageChannel objMsgCh = evt.getChannel();		
		Guild guild = evt.getGuild();
		LadderCheck check = new LadderCheck();
		Member memberAuthor = evt.getMember();
		//test this shit out for later
		//objMsgCh.sendMessage(embed)
		
		
		//this lets you get the raw contnents of a message so just use this to get numbers and strings
		System.out.println(evt.getMessage().getContentRaw());

		// this code detects if the user is a smash admin role and if they are grants
		// them
		// the permissions to do the admin commands
		boolean isAdmin = false;
		if (guild.getMembersWithRoles(guild.getRolesByName("Ladder admin", true)).contains(memberAuthor)) {
			isAdmin = true;
		}
		// then if the user has a specific smash admin role let them do the ban command
		// or whatever else they
		// need to be able to do, also put this at the top to it only does this one
		// to create an admin command put an if(isAdmin == true) before a command
		// but mainly tao dont be dumb and just add an isAdmin check to the command method itself
		// in the Admin commands class
		// also to have acesss to admin commands have a "smash admin" role in the server

		// The authors user id converted to a string of user id numbers
		String author = objMsg.getAuthor().toString();
		UserConvertID authorUser = new UserConvertID(author);
		// The user ID as an string id number that was mentioned by the author of the
		// command
		
		
		

		
//this is the way to detect user input using get content display, however it sometimes does throw
//an exception for list out of bounds index for some reason and im not sure why
		// List <Member> u = objMsg.getMentionedMembers();

		// String a = u.get(0).getEffectiveName();
		// System.out.println(a);

		// if (objMsg.getContentDisplay().equalsIgnoreCase(Ref.prefix + " @" + a)) {
		// objMsgCh.sendMessage("<@" + mentionedUser.conversion() + ">").queue();
		// }

		
		
		

// COMMANDS----------------------------------------------------------------------------
		/*
		 * Checking if user is in the ladder and if not adding them into the ladder
		 */
		
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "commands")) {
			objMsgCh.sendMessage("----------Ladder Bot Commands----------\n"
					+ "(to have admin commands, create a role called Ladder admin)\n" + Ref.prefix
					+ "register: adds you to the ladder\n" + Ref.prefix
					+ "challenge @Username: challenges that user to a match\n" + Ref.prefix
					+ "my rating: shows what your current ladder raitng is\n" + Ref.prefix
					+ "top 10: gets the top 10 ranked users\n" + Ref.prefix
					+ "leaderboard: shows your positon in the ladder\n" + Ref.prefix
					+ "stats @ Username: shows that users stats\n"+ Ref.prefix 
					+ "ban, and unban @Username: admin command for banning and unbanning users from using the ladder in any server\n"+ Ref.prefix
					+ "server ban, and server unban: ban and unban users from using ladder commands in your server\n" + Ref.prefix
					+ "ruleset: gives the current ruleset for matches\n" + Ref.prefix
					+ "admins: Gives a list of the admins on the server\n").queue();
		}

		// check if user is banned and is trying to use a command and tells them no if
		// so
		if (check.checkIfBanned(authorUser.conversion()) == true
				&& objMsg.getContentRaw().substring(0, 1).equals(Ref.prefix)) {
			objMsgCh.sendMessage(objUser.getAsMention() + "you are banned and cannot use ladder commands").queue();
			return;
		}
		
		if (check.checkIfserverBanned(authorUser.conversion(), guild.getId()) == true
				&& objMsg.getContentRaw().substring(0, 1).equals(Ref.prefix)) {
			objMsgCh.sendMessage(objUser.getAsMention() + "you are banned and cannot use ladder commands").queue();
			return;
		}

		AdminCommand admin = new AdminCommand(evt, isAdmin);

		

//Adding new players to the ladder-------------------------------------------------------		
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "register")) {

			// checking if the user is already in the ladder
			LadderCheck checkUser = new LadderCheck(authorUser.conversion());
			if (checkUser.checkIfInLadder() == false) {

				// inserting the user into the database
				objMsgCh.sendMessage(objUser.getAsMention() + " Adding you to the ladder!").queue();
				LadderInsert in = new LadderInsert(authorUser.conversion(), objUser);
				in.insertToLadder();

			} else {
				objMsgCh.sendMessage(objUser.getAsMention() + " You are already in the ladder silly").queue();
			}
		}

//Commands for challenging a player------------------------------------------------------

		// the challenge command object that hold all the commands for challenging a
		// player
		ChallengeCommandGlicko ChallCommand = new ChallengeCommandGlicko(evt);

		/**
		 * the receiving a challenge for a player
		 */
		ChallCommand.challengePlayer();

		/**
		 * the accepting of the challenge occurs only if the the defending player and
		 * the challenger have each others ID's in the challenger id column
		 */
		ChallCommand.acceptChallenge();

		/**
		 * first checks to see if the match was created and or is still running
		 * 
		 * then checks to see if a player stated if they won or not and then asks the
		 * opposite player if the outcome of that game was valid
		 */
		ChallCommand.finishChallWon();

		ChallCommand.finishAuthorWon();

		// checking the other player if the claims add up and either awarding points or
		// doing nothing based on weather or not the claims add up

		// the response to someone finishing a match that the challenged user won
		ChallCommand.challWonResponse();

		// the response to someone finishing a match that the author won
		ChallCommand.authorWonResponse();

//Commands for checking ladder info------------------------------------------------------

		// object for making ladder info commands

		LadderInfoCommand infoCommand = new LadderInfoCommand(evt);
		// prints out the authors ladder points
		infoCommand.myPoints();

		// prints of the top ten ladder users
		infoCommand.top10();

		// getting a list of 10 ladder values with the authors rank being in the middle
		infoCommand.myLadder();

		// tells the stats of the mentioned user
		infoCommand.stats();

//ADMIN COMMANDS-------------------------------------------------------------------------
		String admins = "----------Admins----------";
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "admins")) {
			for (int i = 0; i < guild.getMembersWithRoles(guild.getRolesByName("ladder admin", true)).size(); i++) {
				admins += "\n               " + authorUser.getName(
						guild.getMembersWithRoles(guild.getRolesByName("ladder admin", true)).get(i).toString());
			}
			objMsgCh.sendMessage(admins).queue();
		}

		
		
		
		if (objMsg.getContentDisplay().equalsIgnoreCase(Ref.prefix + "ruleset")) {
			objMsgCh.sendMessage("best of 3, 3 stocks, no items, FS meter off, Hazards off legal stages").queue();
		}
		
		// Admin commands for banning and unbanning users
		if (isAdmin == true) {
			admin.banPlayer();
			admin.unbanPlayer();
			admin.serverBanPlayer();
			admin.serverUnbanPlayer();
		}

		
		objUser = null;
		objMsgCh = null;
		guild = null;
		check = null;
		memberAuthor = null;
		System.gc();
	}
	}
	
}