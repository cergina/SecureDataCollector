var CONTENT_TYPE = "application/json; charset=utf-8";
var DATA_TYPE = "json";

var DATES;
var SENSORS = [{id:5}];

function graphFUNC() {
    $.ajax({
        method: "POST",
        async: false,
        url: $SCRIPT_ROOT + "/action/projects/flats?fid=" + $("#flat-id").val(),
        contentType: CONTENT_TYPE,
        dataType: DATA_TYPE,
        statusCode: {
            201: function(response) {
                DATES = response.data.dates;
                SENSORS = response.data.sensors;
                alert(SENSORS[0]);
            },
        },
    });
}
graphFUNC();


var GraphTotalDatasets = SENSORS.length;

var maxValue = 0;

for (i=0; i < GraphTotalDatasets; i++)
{
    if(Math.max(...SENSORS[i].dataArray) > maxValue){
        maxValue = Math.max(...SENSORS[i].dataArray);
    }
}

var GraphLabelArray = []
for (i=0; i < GraphTotalDatasets; i++)
{
    GraphLabelArray[i] = "Controller #" + SENSORS[i].controllerUnitId + " " + SENSORS[i].name + " (" + SENSORS[i].unitType + ")";
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
                        max: maxValue,
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
    window.myLine = new Chart(ctx, LineChartConfig);
    console.log(SENSORS[0]);
    console.log(SENSORS[1]);
    console.log(SENSORS[2]);
    console.log(SENSORS[3]);
    console.log(SENSORS[4]);
};
