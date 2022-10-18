const mongoose= require('mongoose')

const transportSchema= mongoose.Schema({
  trans_type: {
    type: String,
    required: true,
    default: "xe máy"
  },
  trans_name: {
    type: String,
    required: true
  },
  trans_license: {
    type: String,
    requried: true,
    unique: true
  },
  qr: {
    type: String,
    default: null
  },
  own: {
    type: mongoose.SchemaTypes.ObjectId,
    ref: "user"
  }
})

module.exports= mongoose.model("transport", transportSchema)