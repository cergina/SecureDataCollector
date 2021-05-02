var CONTENT_TYPE = "application/json; charset=utf-8";
var DATA_TYPE = "json";

var DATES;
var FLATS = [{id:5}];

function round(num, multipleOf) {
    return Math.floor((num/multipleOf+1))*multipleOf;
}


// DUPLICATE from collector.js
function getUrlParameter(sParam) { // https://stackoverflow.com/a/21903119/5148218
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return typeof sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
    return false;
}

function graphFUNC() {
    $.ajax({
        method: "POST",
        async: false,
        url: $SCRIPT_ROOT + "/action/buildings?id=" + getUrlParameter('id'),
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        statusCode: {
            201: function(response) {
                DATES = response.data.dates;
                FLATS = response.data.flats;
            },
        },
    });
}
graphFUNC();

var GraphTotalDatasets = FLATS.length;

var maxValue = 0;

// from all measurements values a maximum is chosen, for the y scale amplitude to be chosen
for (i=0; i < GraphTotalDatasets; i++)
{
    if(Math.max(...FLATS[i].dataArray) > maxValue){
        maxValue = Math.max(...FLATS[i].dataArray);
    }
}

var stepRounded = (maxValue/20);

if(maxValue <= 20){
    stepRounded = 1;
}

if(maxValue > 20 && maxValue<=100){
    stepRounded = round(stepRounded, 5);
    maxValue = round(maxValue, 5);
}

if(maxValue > 100){
    stepRounded = round(stepRounded, 10);
    maxValue = round(maxValue, 10);
}

if(maxValue == 0)
{
    stepRounded = 5;
    maxValue = 5;
}

var GraphLabelArray = []
for (i=0; i < GraphTotalDatasets; i++)
{
    GraphLabelArray[i] = FLATS[i].apartmentNO;
}


//TODO Vybrat farby pre oznacenie senzorov na grafe, zatial 15 hodnot - cize podpora pre 15 senzorov
var GraphColourArray = ['#ff0000', '#00ff00', '#0000ff', '#ffd400', '#ff00ff', '#00ffff', '#000000', '#008620', '#001a9f', '#0096ff', '#dccf00', '#8d0088', '#890101', '#beb4b4', '#686868'];

var GraphDatasetsArray = [];

for (i=0; i < GraphTotalDatasets; i++)
{
    GraphDatasetsArray[i] =
        {
            label: GraphLabelArray[i],
            data: FLATS[i].dataArray,
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
            text: "amount of measurements of flats in the building over last 30 days".toUpperCase(),
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
                        labelString: "Amount of measurements",
                    },
                    ticks: {
                        min: 0,
                        max: maxValue,
                        stepSize: stepRounded,
                    },
                },
            ],
        },
    },
};

window.onload = function () {
    var ctx = document.getElementById("graph-last30days").getContext("2d");
    window.myLine = new Chart(ctx, LineChartConfig);
};
