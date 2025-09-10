import { useState } from "react";
import { Modal, Button, Form, Badge } from "react-bootstrap";
import { getOptionsByIds } from "../../../../../services/OptionService";
import { toast } from "react-toastify";
import { LuImagePlus } from "react-icons/lu";
import { LuFileAudio2 } from "react-icons/lu"; // icon audio

const QUESTION_TYPES = ["MULTIPLE_CHOICE", "FILL_IN_THE_BLANK", "ESSAY", "YES_NO", "YES_NO_NOTGIVEN", "TRUE_FALSE", "TRUE_FALSE_NOTGIVEN"];
const QUESTION_SUBTYPES = ["NORMAL", "BLANK_FILLING", "LISTENING"];

const CreateQuestionModal = ({ show, setShow, onCreateQuestion, setShowOptionSelection, selectedOptionIds = [], setSelectedOptionIds }) => {
  const [context, setContext] = useState("");
  const [previewImage, setPreviewImage] = useState("");
  const [imageFile, setImageFile] = useState(null);

  // audio states
  const [audioFile, setAudioFile] = useState(null);
  const [previewAudio, setPreviewAudio] = useState("");

  // new states
  const [type, setType] = useState("MULTIPLE_CHOICE");
  const [subType, setSubType] = useState("NORMAL");

  const handleUploadImage = (event) => {
    if (event.target?.files?.[0]) {
      const file = event.target.files[0];
      setPreviewImage(URL.createObjectURL(file));
      setImageFile(file);
    }
  };

  const handleUploadAudio = (event) => {
    if (event.target?.files?.[0]) {
      const file = event.target.files[0];
      setPreviewAudio(URL.createObjectURL(file));
      setAudioFile(file);
    }
  };

  const handleCheckAtLeastOneTrueOption = async (optionIds) => {
    try {
      if (optionIds.length < 2 || optionIds.length > 4) {
        toast.warning("Số lượng đáp án từ 2 đến 4");
        return false;
      }

      const response = await getOptionsByIds(optionIds);

      if (response?.statusCode === 200) {
        const listOption = response.data || [];
        const correctCount = listOption.filter((opt) => opt.isCorrect === true).length;

        if (correctCount !== 1) {
          toast.warning("Một câu hỏi cần có đúng 1 đáp án đúng.");
          return false;
        }
        return true;
      } else {
        toast.warning("Không thể lấy dữ liệu lựa chọn từ server.");
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API:", error);
      toast.warning("Lỗi hệ thống khi kiểm tra đáp án.");
    }

    return false;
  };

  const handleSubmit = async () => {
    if (!context.trim()) {
      toast.warning("Không được bỏ trống nội dung");
      return;
    }

    if (type === "MULTIPLE_CHOICE") {
      const isValid = await handleCheckAtLeastOneTrueOption(selectedOptionIds);
      if (!isValid) return;
    }

    const questionData = {
      context: context.trim(),
      optionIds: selectedOptionIds,
      imageFile: imageFile,
      audioFile: audioFile, // thêm audioFile
      type,
      subType: type === "MULTIPLE_CHOICE" ? subType : null,
    };

    if (onCreateQuestion(questionData)) {
      handleClose();
    }
  };

  const handleClose = () => {
    setContext("");
    setShow(false);
    setShowOptionSelection(false);
    setSelectedOptionIds([]);
    setImageFile(null);
    setPreviewImage("");
    setAudioFile(null);
    setPreviewAudio("");
    setType("MULTIPLE_CHOICE");
    setSubType("NORMAL");
  };

  const handleShowOptionModal = () => {
    setShowOptionSelection(true);
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Tạo câu hỏi mới</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {/* Type */}
        <Form.Group className="mb-3">
          <Form.Label>Loại câu hỏi (QuestionType)</Form.Label>
          <Form.Select value={type} onChange={(e) => setType(e.target.value)}>
            {QUESTION_TYPES.map((t) => (
              <option key={t} value={t}>
                {t}
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        {/* SubType */}
        {type === "MULTIPLE_CHOICE" && (
          <Form.Group className="mb-3">
            <Form.Label>Kiểu Multiple Choice (QuestionSubType)</Form.Label>
            <Form.Select value={subType} onChange={(e) => setSubType(e.target.value)}>
              {QUESTION_SUBTYPES.map((st) => (
                <option key={st} value={st}>
                  {st}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        )}

        {/* Nếu subtype = LISTENING thì cho upload audio */}
        {type === "MULTIPLE_CHOICE" && subType === "LISTENING" && (
          <Form.Group className="mb-3">
            <div className="col-12">
              <label className="btn btn-outline-success" htmlFor="audioUpload">
                <LuFileAudio2 className="me-2" />
                Tải lên audio
              </label>
              <input type="file" id="audioUpload" className="d-none" accept="audio/*" onChange={handleUploadAudio} />
            </div>
            <div className="col-12">{previewAudio ? <audio controls className="mt-2" src={previewAudio} /> : <div className="text-muted mt-2">Chưa có audio</div>}</div>
          </Form.Group>
        )}

        {/* Nội dung câu hỏi */}
        <Form.Group className="mb-3">
          <Form.Label>Nội dung câu hỏi</Form.Label>
          <Form.Control as="textarea" rows={3} value={context} onChange={(e) => setContext(e.target.value)} placeholder="Nhập nội dung câu hỏi..." />
        </Form.Group>

        {/* Upload ảnh */}
        <Form.Group className="mb-3">
          <div className="col-12">
            <label className="btn btn-outline-primary" htmlFor="imageUpload">
              <LuImagePlus className="me-2" />
              Tải lên ảnh minh họa
            </label>
            <input type="file" id="imageUpload" className="d-none" accept="image/*" onChange={handleUploadImage} />
          </div>
          <div className="col-12">
            {previewImage ? (
              <img src={previewImage} alt="Preview" className="img-thumbnail mt-2" style={{ maxWidth: "200px", maxHeight: "200px" }} />
            ) : (
              <div className="text-muted mt-2">Chưa có ảnh minh họa</div>
            )}
          </div>
        </Form.Group>

        {/* Chọn option */}
        {type === "MULTIPLE_CHOICE" && (
          <>
            <Form.Group className="mb-3">
              <Form.Label>Lựa chọn đã chọn ({selectedOptionIds.length}/4):</Form.Label>
              <div>
                {selectedOptionIds.length > 0 ? (
                  selectedOptionIds.map((id) => (
                    <Badge bg="secondary" key={id} className="me-2">
                      {id}
                    </Badge>
                  ))
                ) : (
                  <div className="text-muted">Chưa chọn lựa chọn nào.</div>
                )}
              </div>
              {selectedOptionIds.length > 0 && (
                <Button className="btn-danger mt-2" onClick={() => setSelectedOptionIds([])}>
                  Xóa hết các lựa chọn
                </Button>
              )}
            </Form.Group>

            <Button variant="outline-primary" onClick={handleShowOptionModal}>
              + Chọn Options
            </Button>
          </>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="primary" onClick={handleSubmit}>
          Tạo câu hỏi
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CreateQuestionModal;
