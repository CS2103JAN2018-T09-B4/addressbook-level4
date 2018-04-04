//@@author marlenekoh
timetable = ["", "", "", "", "", "", "", "", "", "", "", "", "", "CS2103T", "", "", "", "", "CS2103T", "", "", "", "", "CS3241", "", "", "", "", "CS3241", "", "", "", "", "CS3241", "", "", "", "", "CS3241", "", "CS2101", "", "CS3230", "CS2101", "", "CS2101", "", "CS3230", "CS2101", "", "CS2101", "", "CS3230", "CS2101", "", "CS2101", "", "CS3230", "CS2101", "", "CS3247", "CS3241", "", "", "", "CS3247", "CS3241", "", "", "", "CS3247", "", "", "CS3241", "CS3230", "CS3247", "", "", "CS3241", "CS3230", "GES1021", "CS3247", "", "GES1021", "CS2101", "GES1021", "CS3247", "", "GES1021", "CS2101", "GES1021", "CS3247", "", "GES1021", "CS2101", "GES1021", "CS3247", "", "GES1021", "CS2101", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
];
var myTimetable = "";
var nRows = "";
var nCells = "";
function displaySchedule(){
    for (i=0; i<nRows; i++) {
        for (n=0; n<nCells; n++) {
            myTimetable.rows[i+1].cells[n+1].lastChild.data = timetable[n+(i*nCells)];
        }
    }
}
function mapTable(){
    myTimetable = document.getElementById('myTimetable');
    nRows = myTimetable.rows.length-1;
    nCells = myTimetable.rows[0].cells.length-1;
    displaySchedule();
}
onload=mapTable;
