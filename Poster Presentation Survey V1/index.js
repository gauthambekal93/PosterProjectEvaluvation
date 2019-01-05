const express = require('express');
const jwt =require('jsonwebtoken');
const bodyparser=require('body-parser');
const mongoose = require('mongoose');
const app =express();

app.use(bodyparser.json());
app.use(bodyparser.urlencoded({extended:false}))

mongoose.connect('mongodb://gauthambekal93:gautham1@ds135983.mlab.com:35983/posterprojectv1',
                  {useMongoClient:true}).then(()=>{ console.log('db connected'); });

let userSchema =new mongoose.Schema({
    userid: String,
    message:String
})

let teamSchema = new mongoose.Schema({
  teamid:String,
  message:String,
  totalScore:Number,
  score:{
      type:Map,
      of:Number
  },
  evaluationscount:Number
})

let adminSchema = new mongoose.Schema({
    adminid:String,
    adminpass:String
})

let questionSchema = new mongoose.Schema({
question:String
})

let userModel =mongoose.model('user',userSchema);
let teamModel = mongoose.model('team',teamSchema);
let questionModel = mongoose.model('question',questionSchema);
let adminModel =mongoose.model('admin',adminSchema)

app.listen(5000,()=>console.log('Server started on 5000'));


//CREATE NEW USER
app.post('/createUser',(req,res)=>{
    let newUser = new userModel();
    userModel.find({userid:req.body.userid},function(err,doc){
if(doc.length==0)
{
        const user ={
        userid:req.body.userid,
        message:req.body.message
    }
    jwt.sign({user},'secretkey',(err,message)=>{
        res.json({
            message
        });
        });
    newUser.userid = req.body.userid;
    newUser.message = req.body.message; 
    newUser.save(()=>{
    console.log("new user created");
}); 
}else{
    var message ="USER ALREADY CREATED!"
            res.json({
                message
            }); 
    res.end()
}
    });
});


//CREATE NEW TEAM
app.post('/createTeam',(req,res)=>{
    let newTeam = new teamModel({
        score:{}
    });
    teamModel.find({teamid:req.body.teamid},function(err,doc){
        if(doc.length==0){
            const team ={
                teamid:req.body.teamid,
                message:req.body.message
            }
            jwt.sign({team},'secretkey',(err,message)=>{
                res.json({
                    message
                });
                });
            //SAVE THE NEW TEAM TO MONGODB
            newTeam.teamid = req.body.teamid;
            newTeam.message = req.body.message; 
            newTeam.totalScore = 0;
            newTeam.evaluationscount = 0;
    
            newTeam.save(()=>{
            console.log("new team created");
        }); 
        }
        else{var message ="TEAM ALREADY CREATED!"
            res.json({
                message
            }); 
            res.end();
        }
    });
});


//CREATE NEW ADMIN
app.post('/createAdmin',(req,res)=>{
    let newAdmin = new adminModel();
    adminModel.find({adminid:req.body.adminid},function(err,doc){
if(doc.length==0)
{
    newAdmin.adminid = req.body.adminid;
    newAdmin.adminpass = req.body.adminpass;

    newAdmin.save(()=>{
    console.log("new Admin created");
    res.end()
}); 
}else{
    var message ="ADMIN ALREADY CREATED!"
            res.json({
                message
            }); 
            res.end()
        }
    });
});



//LOGIN TO ADMIN
app.get('/loginAdmin',(req,res)=>{
    adminModel.find({adminid:req.query.adminid,adminpass:req.query.adminpass},function (err,doc){          
        if(doc.length>0)
        {
            var message ='Login successful'
            res.json({ message});
            res.end();
        }
        else{
            var message ='Login not successful'
            res.json({message});
            res.end();
        }
    })
})


//LOGIN TO USER
app.get('/loginUser',verifyToken,(req,res)=>{
    jwt.verify(req.token,'secretkey',(err,authData)=>{
if(err)
{
    res.sendStatus(403);
}else{
    var jsonValue =JSON.stringify(authData)
    var obj =JSON.parse(jsonValue)
   
    userModel.find({userid:obj.user.userid},function(err,doc)
    { 
        var response = obj.user
    
         if(doc.length ==1)
         {
            res.json({ response });
         }if(doc.length ==0){
             response ="USER NOT PRESENT"
             res.json({ response });
         }
         if(doc.length >1)
         {response ="DUPLICATE USER"
         res.json({ response });}
    });
}
    });
});


//LOGIN TO TEAM
app.get('/loginTeam',verifyToken,(req,res)=>{
    jwt.verify(req.token,'secretkey',(err,authData)=>{
if(err)
{
    res.sendStatus(403);
}else{
    var jsonValue =JSON.stringify(authData)
    var obj =JSON.parse(jsonValue)

    teamModel.find({teamid:obj.team.teamid},function(err,doc)
    {    var response = obj.team
        if(doc.length ==1)
        {
           res.json({ response });
      // res.json({doc})
        }if(doc.length ==0){
            response ="TEAM NOT PRESENT"
            res.json({ response });
        }
        if(doc.length >1)
        {response ="DUPLICATE TEAM"
        res.json({ response });}
    });
}
    });
});


//ADD QUESTIONS
app.post('/createQuestion',(req,res)=>{
    
    let newQuestion = new questionModel();
    questionModel.find({question:req.body.question},function(err,doc){
        if(doc.length==0){
        newQuestion.question = req.body.question    
    newQuestion.save(()=>{
        var message ="NEW QUESTION ADDED" 
        res.json({
            message
        }); 
        res.end();
    })    
    }
    else{var message ="QUESTION ALREADY EXISTS!"
            res.json({
                message
            }); 
            res.end();
        }
    })  
});


//DOWNLOAD QUESTIONS
app.get('/downloadQuestions',verifyToken,(req,res)=>{
    jwt.verify(req.token,'secretkey',(err,authData)=>{
        if(err)
        {
            res.sendStatus(403);
        }
        else{
            var jsonValue =JSON.stringify(authData)
            if(jsonValue.includes('user'))
            {

            var obj =JSON.parse(jsonValue)
            userModel.find({userid:obj.user.userid},function(err,doc){
               
                var response = obj.user
                if(doc.length ==1)
                {
                questionModel.find(function (err,doc){
                
                    res.json({ doc});
                    res.end();
                });

                }if(doc.length ==0){
                    response ="USER HAS BEEN DELETED"
                    res.json({ response });
                }
                if(doc.length >1)
                {response ="DUPLICATE USER"
                res.json({ response });
                }
            })
        }else{
            response ="NOT AN USER"
                res.json({ response });
        }
        }
    })
})


//UPDATE TEAM SCORE
app.get('/updateScore',verifyToken,(req,res)=>{
    jwt.verify(req.token,'secretkey',(err,authData)=>{
        if(err)
        {
            res.sendStatus(403);
        }
        else{   
             teamModel.findOne({teamid:req.query.teamid},(err,doc)=>{
            
                var temp =0;
            
                if(typeof doc.score.get(req.query.userid)== 'undefined'){
            
                 doc.totalScore = parseInt(doc.totalScore)+ parseInt(req.query.totalScore)
                 doc.evaluationscount =doc.evaluationscount+1
                    }
                    else{
                        doc.totalScore= parseInt(doc.totalScore) 
                              - parseInt(doc.score.get(req.query.userid))
                        doc.totalScore = parseInt(doc.totalScore)
                              +parseInt(req.query.totalScore)
                    }
                  doc.score.set(req.query.userid,req.query.totalScore)

              doc.save(()=>{
                  console.log('Score saved successfully')
                  console.log(doc)
                  
              })
             
            });     
        }
        res.end()
    })
})


//DOWNLOAD ALL TEAM SCORES
app.get('/downloadTeamScores',verifyToken,(req,res)=>{
    jwt.verify(req.token,'secretkey',(err,authData)=>{
        if(err)
        {
            res.sendStatus(403);
        }else
        {
            var jsonValue =JSON.stringify(authData)
            if(jsonValue.includes('user'))
            {
                var obj =JSON.parse(jsonValue)
                userModel.find({userid:obj.user.userid},function(err,doc){
                    if(doc.length ==1)
                    {
                        teamModel.find(function (err,doc){
                
                            res.json({ doc});
                            res.end();
                        });
                    }   
                })
            }
        }
    })
})


function verifyToken(req,res,next)
{
    const bearerHeader = req.headers['authorization'];
    if(typeof bearerHeader !== 'undefined')
    {
const bearer = bearerHeader.split(' ');
const bearerToken = bearer[1];
req.token = bearerToken;
next();
    }else
    {
        res.sendStatus(403);
    }
}