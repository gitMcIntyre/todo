# todo
Todo Test Project Web

To construct the web project:

Once you have the project source files you will also need to unzip the dojo source javascript. It's in the zip file under the Todo Project. Unzip the files under WebContent/javascript/dojo. You should end up with a directory structure that looks like this:

WebContent
  javascript
    com
    dojo
      dijit
      dojo
      dojox
      util
      

The web application home page is http://<server>/todo/home/load.htm

A data grid willl be displayed showing the currently saved tasks.
The description and status of a task can be edited by double clicking on the row/cell or by using arrow keys and the enter key to put the cell in edit mode.

Changes to a cell will be applied whey the cell loses focus.

Clicking on a column header will sort the displayed data.

Changes will be persisted to the underlying data store only when the save button is clicked. Until then data state is only maintained in the UI.

There's one issue when typing in the description cell. The first time you try to type a space the keystroke is ignored. If you type a space again it works.

I believe I have some unnecessary jars in the lib directory. I started with an existing spring project that I was working on some time ago. These can be cleaned up.

I didn't attempt to unit test every class. Some are tirvial at this point. I did however attempt to display my knowledge of JUnit and EasyMock.

Future imporvements:
- create a build process for the client code that would decrease the download size and increase performance. Also create a build using ant for creating a production ready delpoyment package.
- package all third party jars so they can be configured as shared libraries on the web server.
- add functionality to help users complete tasks on time. Maybe a notification system.
- ranking of importance for tasks.
