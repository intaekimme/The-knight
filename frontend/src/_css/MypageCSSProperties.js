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
    fontSize: 16,
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
    boxShadow: '1px 1px 20px #DCD7C9',
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

const btnLeft = {
    width: 140,
    fontSize: 18,
    fontWeight: 'bold',
    color: '#424242',
    bgcolor: '#DCD7C9',
    border: '1px solid #DCD7C9',
    '&:hover': {
        color: '#424242',
        bgcolor: '#D2CCBB',
        border: '1px solid #D2CCBB',
    }
}

const btnRight = {
    width: 140,
    fontSize: 18,
    fontWeight: 'bold',
    color: '#DCD7C9',
    bgcolor: 'red',
    border: '1px solid red',
    '&:hover': {
        color: '#E2D6B5',
        bgcolor: '#B50000',
        border: '1px solid #B50000',
    }
}

export { imgMypage, memberImg, formMem, btnModify, cardMem, textResult, textMaxMem, btnLeft, btnRight }