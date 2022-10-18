const transModel= require("../models/transport.model")
const ErrorResponse= require("../helpers/ErrorResponse")
module.exports= {
  getAll: async(req, res, next)=>{
    let idUser= req.query?.iduser;
    if (idUser){
      let trans= await transModel.find({
        own: idUser
      }).populate("own")
      return res.status(200).json(trans)
    }
    return res.status(200).json(await transModel.find().populate("own"))
  },
  createTrans: async(req, res, next)=>{
    let idUser= req.query.iduser;
    if (!idUser){
      throw new ErrorResponse(404, "isuser must provide")
    }

    let {...body}= req.body
    body.own= idUser
    let trans= await transModel.findOne({
      trans_license: body.trans_license
    })
    if (trans){
      throw new ErrorResponse(401, "Duplicate transport")
    }

    trans= await transModel.create(body)
    let newTran= await transModel.findById(trans._id).populate("own");
    return res.status(201).json(newTran)
  },
  updateTrans: async(req, res, next)=>{
    let id= req.params.id;
    let {...body}= req.body
    let trans= await transModel.findByIdAndUpdate(id, body, {new: true})
    if (!trans){
      throw new ErrorResponse(404, "Not found transport")
    }
    return res.status(200).json(trans)
  },
  deleteTransById: async(req, res, next)=>{
    let id= req.params.id
    let trans= await transModel.findByIdAndDelete(id)
    return res.status(200).json(trans)
  },
  getTransById: async(req, res, next)=>{
    let id= req.params.id
    let trans= await transModel.findById(id)
    if (!trans){
      throw new ErrorResponse(404, "not found transport")
    }
    return res.status(200).json(trans)
  }
}