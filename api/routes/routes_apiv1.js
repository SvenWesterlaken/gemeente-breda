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

router.get('/reports', function(request, response) {
    var longitudeQuery = request.query.longitude || '';
    var latitudeQuery = request.query.latitude || '';

    var query_str;
    if (longitudeQuery !== '' && latitudeQuery !== '') {
        query_str += 'SELECT * FROM report INNER JOIN location ON report.locationID = location.locationID WHERE longitude = ' + longitudeQuery + ' AND latitude = ' + latitudeQuery + ';';
    } else {
        query_str = 'SELECT * FROM report;';
    }

    pool.getConnection(function (err, connection) {
        if (err) {
            throw error
        }
        connection.query(query_str, function (err, rows, fields) {
            connection.release();
            if (err) {
                throw error
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
 