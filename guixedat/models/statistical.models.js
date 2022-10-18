const mongoose= require('mongoose')

const statisticalSchema= mongoose.Schema({
  timeCome: {
    type: String,
    default: null
  },
  timeOut: {
    type: String,
    default: null
  },
  isOut: {
    type: Number,
    default: 0
  },
  transport: {
    type: mongoose.SchemaTypes.ObjectId,
    ref: "transport"
  },
  position: {
    type: String,
    default: null
  }
}, {
  versionKey: false
})

module.exports= mongoose.model("statistical", statisticalSchema)