const mongoose= require('mongoose')

const userSchema= mongoose.Schema({
  username: {
    type: String,
    required: true, 
    unique: true
  },
  password: {
    type: String,
    required: true
  },
  phone: {
    type: String
  },
  address: {
    type: String
  },
  dob: {
    type: String
  }, 
  money: {
    type: Number,
    default: 0
  },
  role: {
    type: String,
    default: "user"
  },
  hetTien: {
    type: Number,
    default: 0
  },
  ngayHet: {
    type: String,
    default: new Date()
  },
  isBlock: {
    type: Number,
    default: 0
  }, 
  email: {
    type: String
  }
}, {
  versionKey: false
})



module.exports= mongoose.model("user", userSchema)