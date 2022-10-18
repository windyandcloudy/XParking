const express= require("express")
const router= express.Router()

const {
  getAll,
  parking,
  deparking,
  deleteStacById
}= require("../controllers/statistical.controller")

const asyncMiddelware= require("../middlewares/asyncHandle")

router
  .route("/")
  .get(asyncMiddelware(getAll))
  .post(asyncMiddelware(parking))
  .patch(asyncMiddelware(deparking))
router
  .route("/:id")
  .delete(asyncMiddelware(deleteStacById))

module.exports= router