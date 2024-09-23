import React from 'react'
import { images } from '../../assets/images/images'; 


const Navbar = () => {
    return (
        <header
          className="row"
          style={{
            alignItems: 'center'
          }}
        >
          <div
            className="col"
            style={{
              alignItems: 'center',
              display: 'flex'
            }}
          >
            <button className="btn">
              <a href="">
                <img
                  alt="tunebox"
                  src={images.logoTuneBox}
                  style={{
                    marginLeft: '50px',
                    marginRight: '50px'
                  }}
                  width="150"
                />
              </a>
            </button>
            <button className="btn">
              <a
                className="text-decoration-none text-black"
                href="/template/mangxahoi.html"
              >
                <span
                  className="text-decoration-none"
                  style={{
                    marginRight: '30px'
                  }}
                >
                  <img
                    src="/images/home.png"
                    style={{
                      marginBottom: '15px',
                      marginRight: '15px'
                    }}
                  />
                  <b>
                    Feed
                  </b>
                </span>
              </a>
            </button>
            <button className="btn">
              <a
                className="text-decoration-none text-black"
                href="/template/TrangBanhHang.html"
              >
                <span>
                  <img
                    src="/images/speaker.png"
                    style={{
                      marginBottom: '15px',
                      marginRight: '15px'
                    }}
                    width="35px"
                  />
                  <b>
                    Shops
                  </b>
                </span>
              </a>
            </button>
          </div>
          <div
            className="col text-end"
            style={{
              alignItems: 'center'
            }}
          >
            <span>
              <img
                src="/images/notification.png"
                style={{
                  marginBottom: '15px',
                  marginRight: '30px'
                }}
              />
            </span>
            <span>
              <img
                src="/images/conversation.png"
                style={{
                  marginBottom: '15px',
                  marginRight: '30px'
                }}
              />
            </span>
            <button
              className="btn btn-warning "
              style={{
                marginBottom: '15px',
                marginRight: '20px'
              }}
              type="button"
            >
              <span>
                <b>
                  Get
                </b>
              </span>
              {' '}
              <img
                height="32px"
                src="/images/crown.png"
                style={{
                  marginLeft: '10px'
                }}
                width="32px"
              />
            </button>
            <button className="btn">
              <a href="/template/profile_User_Activity.html">
                <img
                  alt="Avatar"
                  className="avatar_small"
                  src="/images/avt.jpg"
                  style={{
                    height: '50px',
                    marginBottom: '15px',
                    width: '50px'
                  }}
                />
              </a>
            </button>
            <button className="btn">
              <a href="/template/gioHang.html">
                <span>
                  <img
                    src="/images/shopping-bag.png"
                    style={{
                      marginBottom: '15px',
                      marginRight: '30px'
                    }}
                  />
                </span>
              </a>
            </button>
            <button
              className="btn btn-danger"
              style={{
                marginBottom: '15px',
                marginRight: '10px'
              }}
              type="button"
            >
              {' '}
              <img
                height="20px"
                src="/images/plus-white.png"
                style={{
                  marginBottom: '3px',
                  marginRight: '10px'
                }}
                width="20px"
              />
              {' '}
              <b>
                Create
              </b>
              {' '}
            </button>
          </div>
        </header>
      )
}

export default Navbar
