package org.linuxmce.dce.util

import groovy.sql.Sql
import org.apache.velocity.VelocityContext

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine

/**
 * Used to generate a DCE interface stub for a given device template.
 *
 */
class DCEDatabase {
  Sql sql
  VelocityEngine ve

  /*DeviceTemplate
  DeviceTemplate_DeviceCommandGroup
  DeviceCommandGroup*/

  public DCEDatabase() {
    //TODO, test dcerouter, 192.168.80.1, localhost

    def routerName = "192.168.1.111"

    if (!connectRouter(routerName)) {
      routerName = "localhost"
      if (!connectRouter(routerName)) {
        routerName = "192.168.80.1"
        if (!connectRouter(routerName)) {
          throw new IOException("Cannot connect to DCE Router at dcerouter, 192.168.80.1 or localhost, aborting")
        }
      }
    }

    println "Connected to DCE Router at ${routerName}"
    ve = new VelocityEngine();
    ve.init();

  }

  boolean connectRouter(def name) {
    print "Trying ${name} . . ."
    try {
      sql = Sql.newInstance("jdbc:mysql://${name}/pluto_main", "david",
              "PASSWORD", "com.mysql.jdbc.Driver")

      println "SUCCESS"
      return true
    } catch (Exception ex) {
      println " FAILED"
      ex.printStackTrace()
      return false
    }
  }

  void generateCommandClass(File velocityTemplate, File target) {

    VelocityContext context = new VelocityContext();

    context.put("commands", getCommands())
    context.put("events", getEvents())

    context.put("commandparams", getCommandParams())
    context.put("eventparams", getEventParams())

    //StringWriter w = new StringWriter();
    FileWriter writer = new FileWriter(target)

    ve.evaluate(context, writer, "DEV", velocityTemplate.text)
    writer.flush()
    writer.close()
  }

  Map getEvents() {
    def rows = sql.rows("select PK_Event, Description from Event")

    def events = [:]

    rows.each {
      def name = generateName(it.Description)
      events[name] = it.PK_Event
    }

    return events
  }

  Map getCommands() {
    def rows = sql.rows("select PK_Command, Description from Command")

    def commands = [:]

    rows.each {
      def name = generateName(it.Description)
      commands[name] = it.PK_Command
    }

    return commands
  }

  Map getEventParams() {
    def rows = sql.rows("select PK_EventParameter, Description from EventParameter")

    def commands = [:]

    rows.each {
      def name = generateName(it.Description)
      commands[name] = it.PK_EventParameter
    }

    return commands
  }

  Map getCommandParams() {
    def rows = sql.rows("select PK_CommandParameter, Description from CommandParameter")

    def commands = [:]

    rows.each {
      def name = generateName(it.Description)
      commands[name] = it.PK_CommandParameter
    }

    return commands
  }

  String generateName(String original) {
    return original.replace(" ", "_")
            .replace(".", "")
            .replace("+", "_")
            .replace(":", "")
            .replace("(", "")
            .replace(")", "")
            .replace("/", "_")
            .replace("-", "_")
            .replace("*", "_STAR_")
            .replace("<<", "LEFT")
            .replace(">>", "RIGHT")
            .replace("@", "AT")
            .toUpperCase()
  }

  def loadDevice(int id) {
    println "Loading device ID ${id} from the database . . ."
    def row = sql.firstRow("select PK_DeviceTemplate, Description from DeviceTemplate where PK_DeviceTemplate = ?", [id])
    println row

//    def commands = sql.rows("""select dt_dcg.FK_DeviceCommandGroup from DeviceTemplate_DeviceCommandGroup dt_dcg,
//DeviceCommandGroup_Command dcg_cg, CommandGroup cg, Command c where dt_dcg.FK_DeviceTemplate = ?
//and dt_dcg.FK_DeviceCommandGroup = dcg_cg.FK_DeviceCommandGroup
//and dcg_cg
//
//""", [id])
    def commands = sql.rows("""select Command.PK_Command, Command.Description, DeviceCommandGroup_Command.Description as FullDescription
    from Command, DeviceCommandGroup_Command, DeviceCommandGroup, DeviceTemplate_DeviceCommandGroup
    where Command.PK_Command = DeviceCommandGroup_Command.FK_Command
    AND DeviceCommandGroup_Command.FK_DeviceCommandGroup=DeviceCommandGroup.PK_DeviceCommandGroup
    AND DeviceCommandGroup.PK_DeviceCommandGroup=DeviceTemplate_DeviceCommandGroup.FK_DeviceCommandGroup
    and DeviceTemplate_DeviceCommandGroup.FK_DeviceTemplate=?
""", [id])


    commands.each {
      println it
    }
  }

  def generateDevice(def profile, def id) {
    println "Generating template now"
  }

  /**
   * Statics, a primitive testing harness, not used during the build.
   */
  public static void main(String[] args) {
    println "Hi There!"

    DCEDatabase db = new DCEDatabase()

    //def file = new File("/home/david/Development/Source/opensource/linuxmce/javadceold/library/jdce-se/src/velocity/DCECommands.velocity")

    //println file.absolutePath

    //db.generateCommandClass(file, new File("/home/david/Desktop/something.java"))
    def device = db.loadDevice(2207)
    println "Loaded Device"

  }
}

///Get command groups based on device template
//  select DeviceCommandGroup.* from DeviceCommandGroup, DeviceTemplate_DeviceCommandGroup where DeviceCommandGroup.PK_DeviceCommandGroup=DeviceTemplate_DeviceCommandGroup.FK_DeviceCommandGroup and DeviceTemplate_DeviceCommandGroup.FK_DeviceTemplate=2207;

//select * from DeviceCommandGroup_Command where FK_DeviceCommandGroup=5;

