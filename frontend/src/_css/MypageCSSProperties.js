import img from "../_assets/mypage/mypage.png"

const imgMypage = {
    backgroundImage: `url(${img})`,
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
}
const memberImg = {
    position: 'relative',
    border: "10px solid #000",
}

const formMem = {
    pl: 2,
    fontSize: 18,
    fontWeight: 'bold',
    color: '#4F585B',
}

const btnModify = {
    fontWeight: 'bold',
    color: '#000',
    '&:hover': {
        color: '#D2AB66',
        bgcolor: 'transparent',
    }
}

const cardMem = {
    backgroundColor: '#fff',
    // opacity: 0.8,
    boxShadow: '1px 1px 5px #424242',
}

const textResult = {
    fontSize: 25,
    fontWeight: 'bold',
    color: '#424242'
}
const textMaxMem = {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#424242'
}

export { imgMypage, memberImg, formMem, btnModify, cardMem, textResult, textMaxMem }