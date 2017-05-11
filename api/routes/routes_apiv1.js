/**
 * Created by Koen on 11-5-2017.
 */

var express = require('express');
var router = express.Router();
var connector = require('../db/db_connector');
var pool = require('../db/db_connector');

router.get('*', function(req, res){
    res.status(200);
    res.json({
        "description": "API V1"
    });
});

module.exports = router;
