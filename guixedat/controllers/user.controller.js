const userModel= require("../models/user.model")
const transModel= require("../models/transport.model")
const statisticalModel= require("../models/statistical.models")
const ErrorResponse= require("../helpers/ErrorResponse")
module.exports= {
  getAll: async(req, res, next)=>{
    return res.status(200).json(await userModel.find())
  },
  login: async(req, res, next)=>{
    let {...body}= req.body
    let user= await userModel.findOne({
      username: body.username,
      password: body.password
    })
    if (!user){
      throw new ErrorResponse(404, "Username or password incorrect");
    }
    if (user.isBlock){
      throw new ErrorResponse(400, "Tài khoản đã bị khóa do nợ quá 1 tuần")
    }
    return res.status(200).json(user)
  },
  logup: async(req, res, next)=>{
    let {...body}= req.body
    let user= await userModel.findOne({
      username: body.username
    })
    if (user){
      throw new ErrorResponse(401, "username already exists")
    }
    user= await userModel.create(body)
    return res.status(201).json(user)
  },
  getUserById: async(req, res, next)=>{
    let id= req.params.id;
    let user= await userModel.findById(id)
    if (!user){
      throw new ErrorResponse(404, "Not found user")
    }
    return res.status(200).json(user)
  },
  updateUserById: async(req, res, next)=>{
    let id= req.params.id;
    let {...body}= req.body
    let user= await userModel.findByIdAndUpdate(id, body, {new: true})
    if (!user){
      throw new ErrorResponse(404, "Not found user")
    }
    return res.status(200).json(user)
  },
  deleteUserById: async(req, res, next)=>{
    let id= req.params.id
    let trans= await transModel.findOne({own: id}) 
    await transModel.deleteMany({own: id})
    if (trans)
    await statisticalModel.deleteMany({transport: trans._id})
    let user= await userModel.findByIdAndDelete(id)
    return res.status(200).json(user)
  }
}