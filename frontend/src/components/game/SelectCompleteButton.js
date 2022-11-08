function SelectCompleteButton() {
  function onClick() {
    console.log("선택완료");
  }

  return (
    <div
      style={{
        width: "30vw",
        height: "7vh",
        backgroundColor: "grey",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
      onClick={onClick}
    >
      선택완료
    </div>
  )
}

export default SelectCompleteButton;
