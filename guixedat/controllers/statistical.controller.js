const ErrorResponse = require("../helpers/ErrorResponse");
const stacModel= require("../models/statistical.models")
const transModel= require("../models/transport.model")
const userModel= require("../models/user.model")
module.exports= {
  getAll: async(req, res, next)=>{
    let isout= req.query.isout;
    if (isout){
      let sta= await stacModel.find({
        isOut: isout
      }).populate({
        path: "transport",
        populate: {
          path: "own"
        }
      })
      return res.status(200).json(sta)
    }
    let stac= await stacModel.find().populate({
      path: "transport",
      populate: {
        path: "own"
      }
    })
    return res.status(200).json(stac)    
  },
  parking: async(req, res, next)=>{
    let stacCreate={}
    let bienSo= req.query.trans_license
    if (!bienSo){
      throw new ErrorResponse(401, "Vui lòng cung cấp biển số xe gửi")
    }

    let trans= await transModel.findOne({
      trans_license: bienSo
    })

    if (!trans){
      throw new ErrorResponse(404, "Xe chưa được đăng ký gửi trên hệ thống")
    }

    let st= await stacModel.findOne({
      transport: trans._id,
      isOut: 0
    })
    if (st){
      throw new ErrorResponse(401, "Xe đã được gửi trong bãi");
    }
    let user=  await userModel.findById(trans.own)

    let hetTien=0;
    if (user.money<5000){
      hetTien= 1;
      
    }
    //trừ tiền gửi xe, mỗi lần gửi trừ 5000
    let bdUpUser= {
      money: user.money-5000,
      hetTien: hetTien
    }
    if (hetTien){
      bdUpUser.ngayHet= new Date()
    }
    let userUpdate= await userModel.findByIdAndUpdate(user._id, bdUpUser, {new: true})
    if (!userUpdate){
      throw new ErrorResponse(400, "Tài khoản thanh toán không thành công");
    }
    var today = new Date();
    let d= (Number(today.getDate()+0)<10)?'0'+today.getDate():today.getDate()
    let m= (Number(today.getMonth()+1)<10)?'0'+(today.getMonth()+1):(today.getMonth()+1)
    var date = d+ '-' + m + '-'+today.getFullYear();
    var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    var dateTime = date+' '+time;
    stacCreate.timeCome= dateTime
    stacCreate.own= trans.own
    stacCreate.transport= trans._id
    stacCreate.position= req.body?.position?req.body?.position:null;
    //update xe có mã qr
    let transUpdateQR= await transModel.findByIdAndUpdate(trans._id, {qr: "secretkey-"+bienSo}, {new: true})
    if (!transUpdateQR){
      throw new ErrorResponse(404, "Không tìm thất xe")
    }
    let stac= await stacModel.create(stacCreate)
    let newStac= await stacModel.findById(stac._id).populate({
      path: "transport",
      populate: {
        path: "own"
      }
    })
    if (hetTien){
      throw new ErrorResponse(400, "Xe đã được ghi nợ. Tài khoản của bạn đã hết. Vui lòng nạp trong 1 tuần");
    }
    return res.status(201).json(newStac)
  },
  deparking: async(req, res, next)=>{
    let qr= req.query.qr;
    let bienSo= qr.split("-")[1]
    let trans= await transModel.findOneAndUpdate({trans_license: bienSo}, {qr: null}, {new: true})
    if (!trans){
      throw new ErrorResponse(404, "Không tìm thấy xe. Hãy liên hệ với bảo vệ")
    }

    let stacUpdate={
      isOut: 1,
    }
    var today = new Date();
    let d= (Number(today.getDate()+0)<10)?'0'+today.getDate():today.getDate()
    let m= (Number(today.getMonth()+1)<10)?'0'+(today.getMonth()+1):(today.getMonth()+1)
    var date = d+ '-' + m + '-'+today.getFullYear();
    var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    var dateTime = date+' '+time;
    stacUpdate.timeOut= dateTime
    let stac= await stacModel.findOneAndUpdate({transport: trans._id, isOut: 0}, stacUpdate, {new: true})
    if (!stac){
      throw new ErrorResponse(404, "Lỗi truy vấn biển số xe từ dữ liệu xe")
    }
    let deparkingTransport= await stacModel.findOne({
      _id: stac._id,
      transport: stac.transport,
      isOut: 1
    }).populate({
      path: "transport",
      populate: {
        path: "own"
      }
    })
    return res.status(200).json(deparkingTransport)
  },
  deleteStacById: async(req, res, next)=>{
    let id= req.params.id
    let stac= await stacModel.findByIdAndDelete(id) 
    return res.status(200).json(stac)
  }
  
}