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

router.get('/report/:reportID', function(request, response) {
    var reportID = request.params.reportID;

    var query_str;
    if (reportID) {
        query_str = 'SELECT * FROM country WHERE code = "' + reportID + '";';
    } else {
        query_str = 'SELECT * FROM country;';
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
            res.status(200).json(rows);
        });
    });

    // response.status(200);
    // response.json({"description": "this will return a report", "reportID": reportID});
});

router.get('*', function(req, res){
    res.status(200);
    res.json({
        "description": "API V1"
    });
});

module.exports = router;
 