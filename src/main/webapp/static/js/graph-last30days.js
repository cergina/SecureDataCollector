var CONTENT_TYPE = "application/json; charset=utf-8";
var DATA_TYPE = "json";

var DATES;
var SENSORS = [{id:5}];

function graphFUNC() {
    $.ajax({
        method: "POST",
        async: false,
        url: $SCRIPT_ROOT + "/graph",
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        statusCode: {
            201: function(response) {
                DATES = response.data.dates;
                SENSORS = response.data.sensors;
                alert(' ' + JSON.stringify(response.data.sensors[0]));
            },
        },
    });
}
graphFUNC();

var GraphTotalDatasets = SENSORS.length;

var GraphLabelArray = []
for (i=0; i < GraphTotalDatasets; i++)
{
GraphLabelArray[i] = "Controller #" + SENSORS[i].controllerUnitID + " Sensor: " + SENSORS[i].name;
}


//TODO Vybrat farby pre oznacenie senzorov na grafe, zatial 15 hodnot - cize podpora pre 15 senzorov
var GraphColourArray = ['#ff0000', '#00ff00', '#0000ff', '#ffd400', '#ff00ff', '#00ffff', '#000000', '#008620', '#001a9f', '#0096ff', '#dccf00', '#8d0088', '#890101', '#beb4b4', '#686868'];

var GraphDatasetsArray = [];

for (i=0; i < GraphTotalDatasets; i++)
{

    GraphDatasetsArray[i] =
        {
            label: GraphLabelArray[i],
            data: SENSORS[i].dataArray,
            fill: false,
            borderColor: GraphColourArray[i],
            backgroundColor: GraphColourArray[i],
            borderWidth: 2,
            pointBorderWidth: 4,
        }
}

//SET THE GRAPH CONFIGURATION VALUES
var LineChartConfig = {
    type: 'line',
    data: {
        labels: DATES,
        datasets: GraphDatasetsArray,
    },
    options: {
        responsive: true,
        title: {
            display: true,
            text: "Measurements over last 30 days".toUpperCase(),
            fontColor: "#002080",
        },
        tooltips: {
        mode: "index",
        intersect: false,
        },
    hover: {
    mode: "nearest",
    intersect: true,
},
    scales: {
    xAxes: [
{
    display: true,
    scaleLabel: {
    display: true,
    labelString: "Date",
},
},
    ],
    yAxes: [
{
    display: true,
    scaleLabel: {
    display: true,
    labelString: "Value",
},
    ticks: {
    min: 0,
    max: 200,

    // forces step size to be 5 units
    stepSize: 5,
},
},
    ],
},
    },
    };


    var config = {
    type: "line",
    data: {
    labels: DATES,
    datasets: [
{
    label: "Merač 1",
    backgroundColor: "#FF0000",
    borderColor: "#FF0000",
    borderWidth: 1,
    pointBorderWidth: 2, // default
    data: [10, 15, 20, 60, 70, 70, 90],
    fill: false,
},
{
    label: "Merač 2",
    fill: false,
    backgroundColor: "#00FF00",
    borderColor: "#00FF00",
    borderWidth: 2,
    pointBorderWidth: 4,
    data: [13, 35, 35, 35, 50, 70, 100],
},
{
    label: "Merač 3",
    fill: false,
    backgroundColor: "#0000FF",
    borderColor: "#0000FF",
    borderWidth: 2,
    data: [3, 25, 25, 25, 40, 60, 90],
},
{
    label: "Merač 4",
    backgroundColor: "#303080",
    borderColor: "#303080",
    borderWidth: 3, // default
    data: [10, 20, 30, 38, 70, 70, 100],
    fill: false,
},
{
    label: "Merač 5",
    backgroundColor: "#606060",
    borderColor: "#606060",
    borderWidth: 4,
    data: [7, 9, 9, 16, 19, 30, 40],
    fill: false,
},
    ],
},
    options: {
    responsive: true,
    title: {
    display: true,
    text: "Measurements over last 30 days".toUpperCase(),
    fontColor: "#002080",
},
    tooltips: {
    mode: "index",
    intersect: false,
},
    hover: {
    mode: "nearest",
    intersect: true,
},
    scales: {
    xAxes: [
{
    display: true,
    scaleLabel: {
    display: true,
    labelString: "Date",
},
},
    ],
    yAxes: [
{
    display: true,
    scaleLabel: {
    display: true,
    labelString: "Value",
},
    ticks: {
    min: 0,
    max: 100,

    // forces step size to be 5 units
    stepSize: 5,
},
},
    ],
},
},
};

    window.onload = function () {
    var ctx = document.getElementById("graph-last30days").getContext("2d");
        //window.myLine = new Chart(ctx, config);
        window.myLine = new Chart(ctx, LineChartConfig);
    };