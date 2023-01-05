const express= require("express")
const app= express()
const cors= require('cors')
const connectDB= require("./configs/database")
const router= require("./routers")

app.use(express.json())
app.use(cors())

connectDB()
router(app)

const { CronJob }= require("cron")
const userModel = require("./models/user.model")
const cronJobAuto= new CronJob("0 12 * * *", async function(){
  try{
    let acc= await userModel.find({hetTien: 1})
    for (let a of acc){
      let Difference_In_Time = new Date().getTime()- new Date(a.ngayHet).getTime();
      let Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24);
      if (Difference_In_Days>=7){
        await userModel.findByIdAndUpdate(a._id, {isBlock: 1})
      }
    }
  }catch(error){
    console.log("error: "+error)
  }
}, null, true, "Asia/Ho_Chi_Minh");
cronJobAuto.start()


app.listen(process.env.PORT ||5000, ()=>{
  console.log("Server run at port 5000")
})