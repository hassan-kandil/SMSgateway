var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var db = require('../db');

router.use(bodyParser.urlencoded({ extended: true }));
router.use(bodyParser.json());

router.post('/sendSMS', function (req, res) {
  console.log("got post request"); 
  var sql = "INSERT INTO Message (Phone, Body) VALUES (?, ?);"
  let body = req.body;
  let Phone = body.Phone;
  let msgBody = body.msgBody;
  db.mycon.query(sql, [Phone,msgBody],function (err, result) {
    console.log("Result: " + JSON.stringify(result));
    if(err){
      res.send(err);
    }
      });
      return res.json({"result": "Msg Sent Successfully"});
  });
  

router.get('/getSMS', function (req, res) {
  console.log("got get request"); 
  let sql = "SELECT id, Phone, Body FROM Message WHERE `SentFlag` = 0 ORDER BY `ts` LIMIT 1;"
  db.mycon.query(sql, function (err, result) {
    console.log("Result: " + JSON.stringify(result));
    if(err){
      res.send(err);
    } else {
      res.json(result[0]); 
    }
  });
});


router.get('/sentSMS', function (req, res) {
  console.log("got sentSMS GET request"); 
  let id =req.query.id;
  let sql = "UPDATE Message SET SentFlag = 1 WHERE id=?;";
  db.mycon.query(sql,[id] ,function (err, result) {
    console.log("Result: " + JSON.stringify(result));
    if(err){
      res.send(err);
    } else {
      res.json({"result": "Flag updated!"}); 
    }
  });
});



module.exports = router;
