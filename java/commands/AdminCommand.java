package commands;

import coolGroup.LadderBot.Ref;
import coolGroup.LadderBot.UserConvertID;
import net.dv8tion.jda.core.entities.Guild;
/*import net.dv8tion.jda.core.entities.Member;*/
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sqlClasses.ChallengerUpdate;
import sqlClasses.LadderCheck;
import sqlClasses.Points;

public class AdminCommand {

	private User objUser;
	private MessageChannel objMsgCh;
	private Message objMsg;
	private Guild guild;
	private LadderCheck check;
	private ChallengerUpdate update;
	
	
	
	//note all off the code within the /*...*/  comment brackets are code that could be use but just isnt yet
	//so  we dont want them to create useless objects as of now


	// Checking if the user is an admin
	private boolean isAdmin;
	// The authors user id converted to a string of user id numbers
/*	private String author;
	private UserConvertID authorUser;*/
	// The user ID as an string id number that was mentioned by the author of the
	// command
	private String user;
	private UserConvertID mentionedUser;

	// the User ID of the User that challenged the author of the message
/*	private LadderCheck grabChall;
	private String authorChallUser;*/

	public AdminCommand(MessageReceivedEvent evt, boolean isAdmin) {
		super();
		this.objUser = evt.getAuthor();
		this.objMsgCh = evt.getChannel();
		this.objMsg = evt.getMessage();
		this.guild = evt.getGuild();
		this.check = new LadderCheck();
		this.update = new ChallengerUpdate();
		new Points();
		this.isAdmin = isAdmin;
		// The authors user id converted to a string of user id numbers
/*		this.author = objMsg.getAuthor().toString();
		this.authorUser = new UserConvertID(author);*/
		// The user ID as an string id number that was mentioned by the author of the
		// command
		this.user = objMsg.getMentions().toString();
		this.mentionedUser = new UserConvertID(user);

		// the User ID of the User that challenged the author of the message
/*		this.grabChall = new LadderCheck();
		this.authorChallUser = grabChall.grabChallenger(authorUser.conversion());*/
	}
	/**
	 * if the author of the command is an admin, this command bans a player
	 */
	public void banPlayer() {
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "ban " + "<@" + mentionedUser.conversion() + ">")||
				objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "ban " + "<@!" + mentionedUser.conversion() + ">")
				&& isAdmin == true) {
			update.updateBanTrue(mentionedUser.conversion());
			objMsgCh.sendMessage(objUser.getAsMention() + " This user has been banned!").queue();
		}
	}
	/**
	 * if the author is an admin, this command server bans a player
	 */
	public void serverBanPlayer() {
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "server ban " + "<@" + mentionedUser.conversion() + ">")||
				objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "server ban " + "<@!" + mentionedUser.conversion() + ">")
				&& isAdmin == true) {
			if(	check.checkIfserverBanned(mentionedUser.conversion(), guild.getId()) == false) {
			update.updateAddServerBan(mentionedUser.conversion(), guild.getId());
			}
			objMsgCh.sendMessage(objUser.getAsMention() + " This user has been banned on this server from using LadderBot!").queue();
		}
		
	}
	
	public void serverUnbanPlayer() {
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "server unban " + "<@" + mentionedUser.conversion() + ">")||
				objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "server unban " + "<@!" + mentionedUser.conversion() + ">")
				&& isAdmin == true) {
			if(	check.checkIfserverBanned(mentionedUser.conversion(), guild.getId()) == true) {
			update.updateRemoveServerBan(mentionedUser.conversion(), guild.getId());
			objMsgCh.sendMessage(objUser.getAsMention() + " This user has been unbanned on this server from using LadderBot!").queue();
			} else {
				objMsgCh.sendMessage(objUser.getAsMention() + "This user is not banned from using LadderBot").queue();
			}
			
		}
	}
	
	
	
	/**
	 * if the author of a command is an admin, this command unbanns a banned player
	 */
	public void unbanPlayer() {
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "unban " + "<@" + mentionedUser.conversion() + ">")||
				objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "unban " + "<@!" + mentionedUser.conversion() + ">")
				&& isAdmin == true) {
			update.updateBanFalse(mentionedUser.conversion());
			objMsgCh.sendMessage(objUser.getAsMention() + " This user has been unbanned!").queue();
		}
	}
	public void botBusy() {
		objMsgCh.sendMessage(objUser.getAsMention() + "Bot is busy, try again in a few seconds").queue();
	}
}
