function buildRecitations() {   
    var dataFile = "./js/RecitationsData.json";
    loadData(dataFile, addRecitations);
}
function loadDataTitle(jsonFile, callback) {
    $.getJSON(jsonFile, function(json) {
        callback(json);
    });
}
function addRecitations(data) {
    var recTable = $("#rec_table");
    var rowParity = 0;
    for (var i = 0; i < data.recitations.length; i+=2) {
        var text = "<tr>";
        var rec = data.recitations[i];
        var cellParity = rowParity;
        text += buildRecCell(cellParity, rec);
        cellParity++;
        cellParity %= 2;
        if ((i+1) < data.recitations.length) {
            rec = data.recitations[i+1];
            text += buildRecCell(cellParity, rec);
        }
        else
            text += "<td></td>";
        text += "</tr>";
        recTable.append(text);
        rowParity++;
        rowParity %= 2;
    }
}
function buildRecCell(recClassNum, recData) {
    var text = "<td class='rec_" + recClassNum + "'>"
                + "<table><tr><td valign='top' class='rec_cell'>" 
                + recData.section + "<br />"
                + recData.day_time + "<br />"
                + recData.location + "<br /></td></tr>"
                + "<tr>";
    
    // RECITATION TA #1
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (recData.ta_1 != "none")
        text += "<img src='./images/tas/" 
            + recData.ta_1.replace(/\s/g, '')
            + ".JPG' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + recData.ta_1 + ")<br />";
    else
        text += "TBA";
    text += "</td>";
    
    // RECITATION TA #2
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (recData.ta_2 != "none")
        text += "<img src='./images/tas/" 
            + recData.ta_2.replace(/\s/g, '')
            + ".JPG' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + recData.ta_2 + ")<br />";            
    else
        text += "TBA";
    text += "</table></td>";
    return text;
}
function loadAppDetails(){
    var dataFile = "./js/AppDetailsData.json";
    loadDataTitle(dataFile, replaceTitle);
    loadDataTitle(dataFile, replaceImage);
    loadDataTitle(dataFile, addLink);
}

function addLink(json){
    var array = json.site_pages;
    index = array[0].site_page_use;
    syllabus = array[1].site_page_use;
    schedule = array[2].site_page_use;
    hws = array[3].site_page_use;
    projects = array[4].site_page_use;
    
    if(index === false){
        document.getElementById("indexLink").innerHTML = "";
    }
    if(syllabus === false){
        document.getElementById("syllabusLink").innerHTML = "";
    } 
    if(schedule === false){
        document.getElementById("scheduleLink").innerHTML = "";
    } 
    if(hws === false){
        document.getElementById("hwsLink").innerHTML = "";
    } 
    if(projects === false){
        document.getElementById("projectLink").innerHTML = "";
    } 
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