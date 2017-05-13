/**
 * Created by Koen on 11-5-2017.
 */

var express = require('express');
var router = express.Router();
var pool = require('../db/db_connector');

router.get('/user/:userID', function(request, response) {
    var userID = request.params.userID;

    response.status(200);
    response.json({"description": "this will return a user", "userID": userID});
});

router.get('/reports/:latitude/:longitude/:area', function(request, response) {
    var latitude = request.params.latitude;
    var longitude = request.params.longitude;
    var area = request.params.area;

    var maxLat = +latitude + +area;
    var minLat = +latitude - +area;
    var maxLong = +longitude + +area;
    var minLong = +longitude - +area;

    var query_str;
    if (longitude !== '' && latitude !== '') {
        query_str = 'SELECT * FROM report INNER JOIN location ON report.locationId = location.locationId ' +
            'WHERE longitude BETWEEN ' + minLong + ' AND ' + maxLong + ' AND latitude BETWEEN ' + minLat + ' AND ' + maxLat + ';';
        console.log(query_str);
    } else {
        query_str = 'SELECT * FROM location;';
    }

    pool.getConnection(function (err, connection) {
        if (err) {
            throw err
        }
        connection.query(query_str, function (err, rows, fields) {
            connection.release();
            if (err) {
                throw err
            }
            response.status(200).json(rows);
        });
    });

    // response.status(200);
    // response.json({"description": "this will return a report", "reportID": reportID});
});

router.get('*', function(request, response){
    response.status(200);
    response.json({
        "description": "API V1"
    });
});

module.exports = router;
 