const express= require('express')
const router= express.Router()

const {
  getAll,
  login, 
  logup,
  getUserById,
  updateUserById,
  deleteUserById
} = require("../controllers/user.controller")

const asyncMiddelware= require("../middlewares/asyncHandle")

router
  .route("/")
  .get(asyncMiddelware(getAll))
  .post(asyncMiddelware(logup))

router
  .route("/login")
  .post(asyncMiddelware(login))
  
router
  .route("/:id")  
  .get(asyncMiddelware(getUserById))
  .patch(asyncMiddelware(updateUserById))
  .delete(asyncMiddelware(deleteUserById))

module.exports= router