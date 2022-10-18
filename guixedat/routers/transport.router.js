const express= require('express')
const router= express.Router()

const {
  getAll,
  createTrans,
  updateTrans,
  deleteTransById,
  getTransById
}= require("../controllers/trans.controller")

const asyncMiddelware= require("../middlewares/asyncHandle")

router
  .route("/")
  .get(asyncMiddelware(getAll))
  .post(asyncMiddelware(createTrans))

router
  .route("/:id")
  .patch(asyncMiddelware(updateTrans))
  .delete(asyncMiddelware(deleteTransById))
  .get(asyncMiddelware(getTransById))
  
module.exports= router