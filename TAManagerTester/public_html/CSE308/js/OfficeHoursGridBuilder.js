// DATA TO LOAD
var students;
var studentNames;
var startHour;
var endHour;

function initGrid() {
    students = new Array();
    studentNames = new Array();
    var dataFile = "./js/OfficeHoursGridData.json";
    loadData(dataFile);
}

function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
	initPage(json);
    });
}
function loadDataTitle(jsonFile, callback) {
    $.getJSON(jsonFile, function(json) {
        callback(json);
    });
}
function initPage(data) {
    // GET THE START AND END HOURS
    startHour = parseInt(data.startHour);
    endHour = parseInt(data.endHour);

    // THEN MAKE THE TIMES
    var daysOfWeek = new Array();
    daysOfWeek[0] = "MONDAY";
    daysOfWeek[1] = "TUESDAY";
    daysOfWeek[2] = "WEDNESDAY";
    daysOfWeek[3] = "THURSDAY";
    daysOfWeek[4] = "FRIDAY";
    for (var i = startHour; i < endHour; i++) {
        // ON THE HOUR
        var textToAppend = "<tr>";
        var amPm = getAMorPM(i);
        var displayNum = i;
        if (i > 12)
            displayNum = displayNum-12;
        textToAppend += "<td>" + displayNum + ":00" + amPm + "</td>"
                    + "<td>" + displayNum + ":30" + amPm + "</td>";
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_00" + amPm
                                + "\" class=\"open\"></td>";
        }
        textToAppend += "</tr>"; 
        
        // ON THE HALF HOUR
        var altAmPm = amPm;
        if (displayNum === 11)
            altAmPm = "pm";
        var altDisplayNum = displayNum + 1;
        if (altDisplayNum > 12)
            altDisplayNum = 1;
                    
        textToAppend += "<tr>";
        textToAppend += "<td>" + displayNum + ":30" + amPm + "</td>"
                    + "<td>" + altDisplayNum + ":00" + altAmPm + "</td>";
            
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_30" + amPm
                                + "\" class=\"open\"></td>";
        }
        
        textToAppend += "</tr>";
        var cell = $("#office_hours_table");
        cell.append(textToAppend);
    }
    
    // NOW SET THE OFFICE HOURS
    for (var i = 0; i < data.officeHours.length; i++) {
	var id = data.officeHours[i].day + "_" + data.officeHours[i].time;
	var name = data.officeHours[i].name;
	var cell = $("#" + id);
	if (name === "Lecture") {
	    cell.removeClass("open");
	    cell.addClass("lecture");
	    cell.html("Lecture");
	}
	else {
	    cell.removeClass("open");
	    cell.addClass("time");
	    cell.append(name);
	}
    }
}
function getAMorPM(testTime) {
    if (testTime >= 12)
        return "pm";
    else
        return "am";
}
function loadAppDetails(){
    var dataFile = "./js/AppDetailsData.json";
    loadDataTitle(dataFile, replaceTitle);
    loadDataTitle(dataFile, replaceImage);
}
function replaceTitle(json){
    name = json.course_name;
    number = json.course_number;
    semester = json.sem;
    year = json.year;
    title = json.page_title;
    
    document.getElementById("courseTitle").innerHTML = name + number + "-" + semester + "<br>" + title;
}
function replaceImage(json){
    bannerImage = json.banner_image_path;
    leftFooterImage = json.left_footer_image_path;
    rightFooterImage = json.right_footer_image_path;
    
    document.getElementById("bannerImage").src = bannerImage;
    document.getElementById("leftFooterImage").src = leftFooterImage;
    document.getElementById("rightFooterImage").src = rightFooterImage;
}