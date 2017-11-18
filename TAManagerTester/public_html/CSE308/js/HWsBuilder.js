// DATA TO LOAD
var hws;
var daysOfWeek;
var redInc;
var greenInc;
var blueInc;

function HW(hDate, hTime, hTitle, hTopic, hLink, hCriteria) {
    this.date = hDate;
    this.time = hTime;
    this.title = hTitle;
    this.topic = hTopic;
    this.link = hLink;
    this.criteria = hCriteria;
}
function ScheduleDate(sMonth, sDay) {
    this.month = sMonth;
    this.day = sDay;
}
function initHWs() {
    redInc = 10;
    greenInc = 10;
    blueInc = 5;
    
    daysOfWeek = new Array(7);
    daysOfWeek[0]=  "Sunday";
    daysOfWeek[1] = "Monday";
    daysOfWeek[2] = "Tuesday";
    daysOfWeek[3] = "Wednesday";
    daysOfWeek[4] = "Thursday";
    daysOfWeek[5] = "Friday";
    daysOfWeek[6] = "Saturday";
    
    var dataFile = "./js/ScheduleData.json";
    loadData(dataFile);
}
function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
	loadJSONData(json);
        addHWs();
    });
}
function loadDataTitle(jsonFile, callback) {
    $.getJSON(jsonFile, function(json) {
        callback(json);
    });
}
function loadJSONData(data) {    
    // LOAD HWs DATA
    hws = new Array();
    for (var i = 0; i < data.hws.length; i++) {
        var hwData = data.hws[i];
        var hwDate = new ScheduleDate(hwData.month, hwData.day);
        var hw = new HW(hwDate, hwData.time, hwData.title, hwData.topic, hwData.link, hwData.criteria);
        hws[i] = hw;
    }
}

function addHWs() {
    var tBody = $("#hws");
    var red = 240;
    var green = 240;
    var blue = 255;
    for (var i = 0; i < hws.length; i++) {
        var hw = hws[i];
        var day = hw.date.day;
        var month = hw.date.month;
        var dayOfWeek = getDayOfWeek(day,month);
        
        // THE FIRST CELL
        var textToAppend = "<tr class=\"hw\" style=\"background-color:rgb(" + red + "," + green + "," + blue + ")\">"
                            + "<td class=\"hw\" style=\"padding-right: 60px\">"
                                + "<br />";
        if (hw.link.valueOf() === "none".valueOf()) {
            textToAppend += hw.title;
        }
        else {
            textToAppend += "<a href=\"" + hw.link + "\">" + hw.title + "</a>";
        }
        textToAppend += " - " + hw.topic + "<br /><br /></td>";
        
        // THE SECOND CELL
        textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\">"
                        + "<br />" + dayOfWeek + ", " + month + "/" + day
                        + "<br /><br /><br />"
                        + "</td>";
                       
        textToAppend += "</tr>";
        tBody.append(textToAppend);
        red -= redInc;
        green -= greenInc;
        blue -= blueInc;
    }
}
function getDayOfWeek(gDay, gMonth) {
    var date = new Date();
    date.setDate(gDay);
    date.setMonth(gMonth-1);
    return daysOfWeek[date.getDay()];
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