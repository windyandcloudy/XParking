const mongoose= require("mongoose")
const configuration= require("./configuration")
const userModel= require("../models/user.model")
const transModel= require("../models/transport.model")
const statisticalModel= require("../models/statistical.models")

const connectDB= async ()=>{
  try {
    const conn= await mongoose.connect(configuration.DB_URL, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });
    let ad = await userModel.findOne({ username: "admin" });
    if (!ad) {
      await userModel.create(configuration.USER_ADMIN);
    }
    await transModel.deleteMany({own: null})
    await statisticalModel.deleteMany({transport: null})
    console.log("Kết nối db thành công");
  } catch (error) {
    console.log("error db: "+ error.message);
  }
}


module.exports = connectDB